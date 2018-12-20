// `error_chain!` can recurse deeply
#![recursion_limit = "1024"]

#[macro_use]
extern crate error_chain;

use std::io::{Error, ErrorKind};

mod error {
    error_chain! {
        foreign_links {
            IO(std::io::Error);
        }
    }
}

pub type ResT<T> = error::Result<T>;

//&'static str
const RES_STATUS: &str = "<return><status>";
const RES_STATUS_LEN: usize = 16; // RES_STATUS.len();

const RES_STATUS_SUCCESS: &str = "SUCCESS";
const RES_STATUS_SUCCESS_LEN: usize = 7; // RES_STATUS_SUCCESS.len();

fn get_return_status(response_body: &String) -> ResT<&str> {
    let idx: usize = response_body.find(RES_STATUS).ok_or_else(|| Error::new(ErrorKind::Other, "Status start"))? + RES_STATUS_LEN;
    Ok(response_body.get(idx..(idx + RES_STATUS_SUCCESS_LEN)).ok_or_else(|| Error::new(ErrorKind::Other, "Status get"))?)
}

fn main() -> ResT<()> {
    let src: String = String::from("<?xml version='1.0' encoding='UTF-8'?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><ns2:createProjectResponse xmlns:ns2=\"http://project.cbapi.clarabridge.com/\"><return><status>SUCCESS</status><projectName>bn1</projectName></return></ns2:createProjectResponse></S:Body></S:Envelope>");

    let result_status: &str = get_return_status(&src)?;

    println!("status: {}", result_status);

    assert!(result_status == RES_STATUS_SUCCESS);

    Ok(())
}
