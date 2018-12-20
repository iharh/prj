use crate::errors::ResT;
use handlebars::Handlebars;
use std::path::Path;

#[derive(Serialize)]
pub struct PrjCreateData {
    pub lang_id: String,
    pub prj_name: String,
}

#[derive(Serialize)]
pub struct PmvdData {
    pub prj_name: String,
    pub verbatim_text: String,
}

fn reg_template(hbs: & mut Handlebars, templates_root: &Path, name: &str) -> ResT<()> {
    Ok(hbs.register_template_file(name, templates_root.join(name.to_owned() + ".hbs"))?)
}

pub fn reg_templates(hbs: & mut Handlebars) -> ResT<()> {
    let templates_root = Path::new("./src/templates");
    println!("reg_template enter");
    // TODO: traverse a templates_root and auto-register all the *.hbs files
    reg_template(hbs, templates_root, "prjCreate")?;
    reg_template(hbs, templates_root, "pmvd")?;
    println!("reg_template exit");
    Ok(())
}
