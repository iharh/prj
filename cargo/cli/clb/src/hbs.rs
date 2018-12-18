use crate::errors::ResT;
use handlebars::Handlebars;

// define some data
#[derive(Serialize)]
pub struct PrjCreateData {
    pub lang_id: String,
    pub prj_name: String,
}

pub fn reg_templates(hbs: & mut Handlebars) -> ResT<()> {
    hbs.register_template_file("prjCreate", "./src/templates/prjCreate.hbs")?;
    Ok(())
}
