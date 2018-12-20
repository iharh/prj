use crate::errors::ResT;

use crate::rest::pmvd;
use crate::hbs::reg_templates;

use durations::MILLISECOND as MS;
use crossbeam_channel::{Receiver, TryRecvError};
use reqwest::Client;
use handlebars::Handlebars;

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
    /// The receive side of our mpmc queue.
    pub rx: Receiver<Message>,
}

impl Worker {
    pub fn run(self) -> ResT<()> { // mut self
        let client = Client::new();

        let mut hbs: Handlebars = Handlebars::new();
        reg_templates(& mut hbs)?;

        while let Some(work) = self.get_work()? { // mut work
            self.process_request(&client, &hbs, &work.text)?;
        }
        Ok(())
    }

    fn process_request(&self, client: &Client, hbs: &Handlebars, text: &str) -> ResT<()> {
        pmvd(&client, &hbs, "bn1", text)?;
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
