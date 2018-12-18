// `error_chain!` can recurse deeply
#![recursion_limit = "1024"]

mod errors;
mod worker;

use crate::errors::ResT;
use crate::worker::{Work, Message, Worker};

use std::io::{BufRead, BufReader};
use crossbeam_channel::bounded;
use std::thread;
use std::vec;

#[macro_use]
extern crate error_chain;

fn main() -> ResT<()> {
    let num_threads = 32;
    let num_iters = 100000;
    let (tx, rx) = bounded::<Message>(num_threads * num_threads);
    let mut handles = vec![];

    for idx in 0..num_threads {
        let worker = Worker {
            thread_id: idx,
            rx: rx.clone()
        };
        handles.push(thread::spawn(|| worker.run()));
    }

    for iter in 0..num_iters {
        let file = std::fs::File::open("input.txt")?;
        let reader = BufReader::new(&file);
        for (index, line) in reader.lines().enumerate() {
            let line = line?;
            tx.send(Message::Work(
                Work { iter_id: iter, line_id: index, text: line, }
            ))?;
        }
    }

    for _ in 0..num_threads {
        tx.send(Message::Quit)?;
    }

    for handle in handles {
        handle.join().unwrap()?;
    }
    Ok(())
}

// RUST_BACKTRACE=1 cargo run --example simple
