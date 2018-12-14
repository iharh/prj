// `error_chain!` can recurse deeply
#![recursion_limit = "1024"]

// use reqwest::{Client, StatusCode};
// use std::collections::HashMap;
// use std::io::{BufRead, BufReader};
use std::thread;
use std::vec;
//use std::{fs, io, path::Path};

#[macro_use]
extern crate error_chain;

extern crate crossbeam_channel as channel;

pub struct Work {
    work_id: usize,
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

/*
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
    for (_index, line) in reader.lines().enumerate() {
        let line = line?;
        // println!("{}. {}", index + 1, line);
        // println!("line: {}", index + 1);
        process_request(client, &line)?
    }
    Ok(())
}
*/

impl Worker {
    fn run(self) { // mut self
        while let Some(_work) = self.get_work() { // mut work
            // println!("thread: {} wordId: {}, text: {}", self.thread_id, work.work_id, work.text);
        }
    }

    fn get_work(&self) -> Option<Work> { // &mut self
        match self.rx.try_recv() {
            Ok(Message::Work(work)) => {
                println!("thread: {} wordId: {}, text: {}", self.thread_id, work.work_id, work.text);
                return Some(work);
            }
            Ok(Message::Quit) => {
                println!("thread: {} quit recieved", self.thread_id);
                return None;
            }
            Err(_) => {
                println!("thread: {} err recieved", self.thread_id);
                return None;
            }
        }
    }
}

fn main() -> ResT<()> {
    let num_threads = 4;
    let (tx, rx) = channel::unbounded::<Message>();

    // let client = Client::new();
    for index in 0..50 {
        // println!("iter: {}", index);
        // process_file(&client)?;
        tx.send(Message::Work(
            Work { work_id: index, text: format!("item{}", index), }
        ))?;

    }

    for _ in 0..num_threads {
        tx.send(Message::Quit)?;
    }

    let mut handles = vec![];
    for idx in 0..num_threads {
        let worker = Worker {
            thread_id: idx, rx: rx.clone()
        };
        handles.push(thread::spawn(|| worker.run()));
    }

    for handle in handles {
        handle.join().unwrap();
    }

    Ok(())
}

// RUST_BACKTRACE=1 cargo run --example simple
