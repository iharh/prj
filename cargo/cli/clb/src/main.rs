// `error_chain!` can recurse deeply
#![recursion_limit = "1024"]

#[macro_use]
extern crate error_chain;
#[macro_use]
extern crate serde_derive;

mod args;
mod errors;
mod hbs;
mod rest;
mod worker;

use crate::args::{Args, USAGE};
use crate::errors::ResT;
use crate::hbs::reg_templates;
use crate::rest::{get_client, prj_create, pmvd};
use crate::worker::{Work, Message, Worker};
//use crate::errors::ResT;

use crossbeam_channel::bounded;
use reqwest::Client;
use docopt::Docopt;
use handlebars::Handlebars;

use std::thread;
use std::io::{BufRead, BufReader};

fn process_rt_multi() -> ResT<()> {
    let num_threads = 4;
    let num_iters = 100_000;
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

fn process_rt_single(client: &Client, hbs: &Handlebars) -> ResT<()> {
    let file = std::fs::File::open("input.txt")?;
    let reader = BufReader::new(&file);
    for (index, line) in reader.lines().enumerate() {
        println!("line: {}", index);
        let text = line?;
        pmvd(&client, &hbs, "bn1", &text)?;
    }
    Ok(())
}


fn main() -> ResT<()> {
    let args_res = Docopt::new(USAGE)
        ?.options_first(true).deserialize();

    let args: Args = args_res?;
    // println!("{:?}", args),

    let mut hbs: Handlebars = Handlebars::new();
    reg_templates(& mut hbs)?;

    let client = get_client()?;

    match args.arg_command {
        args::Command::Prj =>
        {
            let resp_body = prj_create(&client, &hbs, "bn", "bn1")?;
            println!("resp_body: {}", resp_body);
        },
        args::Command::RtSingle  =>
        {
            process_rt_single(&client, &hbs)?;
        },
        args::Command::RtMulti  =>
        {
            process_rt_multi()?;
        },
    }
    Ok(())
}
