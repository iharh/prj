// `error_chain!` can recurse deeply
#![recursion_limit = "1024"]

use reqwest::{Client, StatusCode};
use std::io::{BufRead, BufReader};
//use std::{fs, io, path::Path};

#[macro_use]
extern crate error_chain;

mod errors {
    error_chain! {
        foreign_links {
            IO(std::io::Error);
            Reqwest(reqwest::Error);
        }
    }
}
// use errors::*;

type ResT<T> = errors::Result<T>;

fn single(client: &Client, text: &str) -> ResT<()> {
    let resp = client.get("http://localhost:8091/analyze")
        .query(&[("text", text)])
        .send()?;

    assert!(resp.status() == StatusCode::OK);
    // std::io::copy(&mut resp, &mut std::io::stdout())?;
    // println!("respBody = {:?}", respBody);
    Ok(())
}

fn process_file(client: &Client) -> ResT<()> {
    let file = std::fs::File::open("input.txt")?;
    let reader = BufReader::new(file);
    for (_index, line) in reader.lines().enumerate() {
        let line = line?;
        // println!("{}. {}", index + 1, line);
        single(client, &line)?
    }
    Ok(())
}

fn main() -> ResT<()> {
    let client = Client::new();
    for index in 0..1 {
        println!("iter: {}", index);
        process_file(&client)?;
    }
    Ok(())
}

// RUST_BACKTRACE=1 cargo run --example simple
