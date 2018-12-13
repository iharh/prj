// `error_chain!` can recurse deeply
#![recursion_limit = "1024"]

use std::io::{BufRead, BufReader};
//use std::{fs, io, path::Path};

// RUST_BACKTRACE=1 cargo run --example simple

// https://doc.rust-lang.org/std/boxed/
// https://doc.rust-lang.org/std/macro.assert.html
// https://doc.rust-lang.org/std/error/

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

// https://doc.rust-lang.org/book/ch17-02-trait-objects.html
//type DynErrT = Box<dyn std::error::Error>;
//type ResT<T> = Result<T, DynErrT>;
type ResT<T> = errors::Result<T>;

fn single(client: &reqwest::Client, text: &str) -> ResT<()> {
    let resp = client.get("http://localhost:8091/analyze")
        .query(&[("text", text)])
        .send()?;

    assert!(resp.status() == 200);
    // std::io::copy(&mut resp, &mut std::io::stdout())?;
    // println!("respBody = {:?}", respBody);
    Ok(())
}

fn process_file(client: &reqwest::Client) -> ResT<()> {
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
    let client = reqwest::Client::new();
    for _x in 0..1000000 {
        process_file(&client)?;
    }
    Ok(())
}
