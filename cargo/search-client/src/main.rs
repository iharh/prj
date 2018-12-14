// `error_chain!` can recurse deeply
#![recursion_limit = "1024"]

use reqwest::{Client, StatusCode};
use std::collections::HashMap;
use std::io::{BufRead, BufReader};
use std::time::Duration;
use std::thread;
use std::vec;

extern crate crossbeam_channel as channel;
#[macro_use]
extern crate error_chain;

pub struct Work {
    iter_id: usize,
    line_id: usize,
    text: String,
}

pub enum Message {
    Work(Work),
    Quit,
}

mod errors {
    error_chain! {
        foreign_links {
            IO(std::io::Error);
            Reqwest(reqwest::Error);
            ChannelSendError(channel::SendError<super::Message>);
            TryRecvError(channel::TryRecvError);
        }
    }
}
// use errors::*;

struct Worker {
    thread_id: usize,
    /// The receive side of our mpmc queue.
    rx: channel::Receiver<Message>,
}

type ResT<T> = errors::Result<T>;

impl Worker {
    fn run(self) -> ResT<()> { // mut self
        let client = Client::new();
        while let Some(work) = self.get_work()? { // mut work
            self.process_request(&client, &work.text)?;
        }
        Ok(())
    }

    fn process_request(&self, client: &Client, text: &str) -> ResT<()> {
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
        Ok(())
    }

    fn get_work(&self) -> ResT<Option<Work>> { // &mut self
        loop {
            match self.rx.try_recv() {
                Ok(Message::Work(work)) => {
                    println!("thread: {} iter: {} line: {}", self.thread_id, work.iter_id, work.line_id);
                    return Ok(Some(work));
                }
                Ok(Message::Quit) => {
                    println!("thread: {} quit", self.thread_id);
                    return Ok(None);
                }
                Err(channel::TryRecvError::Empty) => {
                    thread::sleep(Duration::from_millis(100));
                }
                Err(channel::TryRecvError::Disconnected) => {
                    return Ok(None);
                }
            }
        }
    }
}

fn main() -> ResT<()> {
    let num_threads = 32;
    let num_iters = 100000;
    let (tx, rx) = channel::bounded::<Message>(num_threads * num_threads);
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
