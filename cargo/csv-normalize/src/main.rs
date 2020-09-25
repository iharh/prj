// extern crate csv;

use std::error::Error;
use std::io;
use std::process;

fn print_records() -> Result<(), Box<dyn Error>> {
    let mut rdr = csv::Reader::from_reader(io::stdin());
    
    // The iterator yields Result<StringRecord, Error>
    for result in rdr.records() {
        let record = result?;
        let extracted = &record[0];
        let single_line = str::replace(extracted, "\n", " ");
        let s = single_line.trim();
        println!("{}", s);
    }

    Ok(())
}

fn main() {
    if let Err(err) = print_records() {
        println!("error printing records: {}", err);
        process::exit(1);
    }
}
