//extern crate reqwest;
use std::io;
use std::fs::File;

const BASE_URL: &str = "https://nexus.dev.clarabridge.net/content/repositories/public/Clarabridge/FX-windows-x64/1.104.0/";

const MODULE_NAMES: &'static [&'static str] = &["break-detector", "cape", "gazetteer", "morph", "morph5", "morph_tr", "pe",
    "post-syntax", "regexp", "syntax", "tokenizer_ja", "tokenizer_zh", "unknown", "unkword_en" ];

fn main() {
    for module_name in MODULE_NAMES {
        let file_name = format!("{}.pdb", module_name);
        let url = format!("{}{}", BASE_URL, file_name);

        println!("downloading {} ...", file_name);
        let mut resp = reqwest::get(&url).expect("request failed");
        let mut out = File::create(format!("./out/{}", file_name)).expect("failed to create file");
        io::copy(&mut resp, &mut out).expect("failed to copy content");
    }
}
