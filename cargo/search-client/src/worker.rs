use crate::errors::ResT;

use durations::MILLISECOND as MS;
use crossbeam_channel::{Receiver, TryRecvError};
use reqwest::{Client, StatusCode};

use std::collections::HashMap;
use std::thread;

pub struct Work {
    pub iter_id: usize,
    pub line_id: usize,
    pub text: String,
}

pub enum Message {
    Work(Work),
    Quit,
}

pub struct Worker {
    pub thread_id: usize,
    pub rx: Receiver<Message>,
}

impl Worker {
    pub fn run(self) -> ResT<()> { // mut self
        let client = Client::new();
        while let Some(work) = self.get_work()? { // mut work
            self.process_request(&client, &work.text)?;
        }
        Ok(())
    }

    fn process_request(&self, client: &Client, text: &str) -> ResT<()> {
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
                Err(TryRecvError::Empty) => {
                    thread::sleep(100*MS);
                }
                Err(TryRecvError::Disconnected) => {
                    return Ok(None);
                }
            }
        }
    }
}
