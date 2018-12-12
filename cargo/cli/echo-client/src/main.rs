//use std::{fs, io, path::Path};

// io::Error
// 

fn main() -> Result<(), Box<std::error::Error>> {
    // let client = reqwest::Client::new();
    let _client = reqwest::Client::builder()
        .build()?;

    //let respBody = client.get("http://localhost:8000/hello")
    //    ?.text()?;

    //println!("respBody = {:?}", respBody);
    println!("hello!");
    Ok(())
}
