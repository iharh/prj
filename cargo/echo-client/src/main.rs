// `error_chain!` can recurse deeply
#![recursion_limit = "1024"]

//use std::{fs, io, path::Path};
use std::io::{BufRead, BufReader};

// RUST_BACKTRACE=1 cargo run

// https://doc.rust-lang.org/std/boxed/
// https://doc.rust-lang.org/std/macro.assert.html
// https://doc.rust-lang.org/std/error/

// io::Error
// https://rust-lang-nursery.github.io/edition-guide/rust-2018/error-handling-and-panics/question-mark-in-main-and-tests.html

// https://github.com/seanmonstar/reqwest/blob/master/examples/simple.rs
// cargo run --example simple

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

// type StdErrT = Box<std::error::Error>;
// type ReqErrT = Box<reqwest::Error>;
// type IOErrT = Box<std::io::Error>;
// https://doc.rust-lang.org/book/ch17-02-trait-objects.html
//type DynErrT = Box<dyn std::error::Error>;
//type ResT<T> = Result<T, DynErrT>;
type ResT<T> = errors::Result<T>;

fn single(text: &str) -> ResT<()> {
    // let mut resp = reqwest::get("http://localhost:8000/hello")?;
    let url = String::from("http://localhost:8091/analyze?text=") + text;
    let resp = reqwest::get(&url)?;
    assert!(resp.status() == 200);
    // std::io::copy(&mut resp, &mut std::io::stdout())?;
    // println!("respBody = {:?}", respBody);
    Ok(())
}

fn process_file() -> ResT<()> {
    let file = std::fs::File::open("input.txt")?;
    let reader = BufReader::new(file);
    // Read the file line by line using the lines() iterator from std::io::BufRead.
    for (index, line) in reader.lines().enumerate() {
        let line = line.unwrap(); // Ignore errors.
        // Show the line and its number.
        println!("{}. {}", index + 1, line);
        single(&line)?
    }
    Ok(())
}

fn main() -> ResT<()> {
    // let client = reqwest::Client::builder().build()?;
    // single("abc")
    process_file()
}
