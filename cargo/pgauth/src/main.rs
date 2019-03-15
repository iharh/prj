// `error_chain!` can recurse deeply
#![recursion_limit = "1024"]

#[macro_use]
extern crate error_chain;

//extern crate errors;

extern crate postgres;

mod errors;

use postgres::{Connection, TlsMode};

use crate::errors::ResT;

fn main() -> ResT<()> {
    println!("Hello, world!");
    let conn = Connection::connect("postgres://postgres@localhost/oauth", TlsMode::None)?;
    Ok(())
}
