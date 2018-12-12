//use std::{fs, io, path::Path};

// https://doc.rust-lang.org/std/boxed/
// https://doc.rust-lang.org/std/macro.assert.html
// https://doc.rust-lang.org/std/error/

// io::Error
// https://rust-lang-nursery.github.io/edition-guide/rust-2018/error-handling-and-panics/question-mark-in-main-and-tests.html

// https://github.com/seanmonstar/reqwest/blob/master/examples/simple.rs
// cargo run --example simple

// type StdErrT = Box<std::error::Error>;
// type ReqErrT = Box<reqwest::Error>;
// type IOErrT = Box<std::io::Error>;
// https://doc.rust-lang.org/book/ch17-02-trait-objects.html
type DynErrT = Box<dyn std::error::Error>;
type ResT = Result<(), DynErrT>;

fn single(text: &str) -> ResT {
    // let mut resp = reqwest::get("http://localhost:8000/hello")?;
    let url = String::from("http://localhost:8091/analyze?text=") + text;
    let resp = reqwest::get(&url)?;
    assert!(resp.status() == 200);
    // std::io::copy(&mut resp, &mut std::io::stdout())?;
    // println!("respBody = {:?}", respBody);
    Ok(())
}

fn main() -> ResT {
    // let client = reqwest::Client::builder().build()?;
    single("abc")
}
