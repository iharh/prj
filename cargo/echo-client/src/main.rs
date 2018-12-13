// `error_chain!` can recurse deeply
#![recursion_limit = "1024"]

use reqwest::{Client, StatusCode};
use std::collections::HashMap;
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

fn process_request(client: &Client, text: &str) -> ResT<()> {
    // http://localhost:8091/analyze
    //let resp = client.get("http://localhost:8080/analyze")
    //    .query(&[("text", text)])
    //    .send()?;

    let mut map = HashMap::new();
    map.insert("text", text);

    let resp = client.post("http://localhost:8080/process")
        .json(&map)
        .send()?;

    assert!(resp.status() == StatusCode::OK);
    // std::io::copy(&mut resp, &mut std::io::stdout())?;
    // println!("respBody = {:?}", respBody);
    Ok(())
}

fn process_file(client: &Client) -> ResT<()> {
    let file = std::fs::File::open("input.txt")?;
    let reader = BufReader::new(file);
    for (index, line) in reader.lines().enumerate() {
        let line = line?;
        // println!("{}. {}", index + 1, line);
        // println!("line: {}", index + 1);
        process_request(client, &line)?
    }
    Ok(())
}

fn main() -> ResT<()> {
    let client = Client::new();
    for index in 0..100000 {
        println!("iter: {}", index);
        process_file(&client)?;
    }
    Ok(())
}

// RUST_BACKTRACE=1 cargo run --example simple
