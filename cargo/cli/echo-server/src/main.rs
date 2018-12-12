#![feature(proc_macro_hygiene, decl_macro)]

#[macro_use] extern crate rocket;

#[cfg(test)] mod tests;

#[get("/hello")]
fn hello() -> &'static str {
    "hello"
}

#[get("/world")]
fn world() -> &'static str {
    "world"
}

fn main() {
    rocket::ignite().mount("/", routes![hello, world]).launch();
    // println!("Done!");
}
