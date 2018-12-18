// define some data
#[derive(Serialize)]
pub struct PrjCreateData {
    lang_id: String,
    prj_name: String,
}

pub fn reg_templates(hbs: & mut Handlebars) -> ResT<()> {
    hbs.register_template_file("prjCreate", "./src/templates/prjCreate.hbs")?;
    Ok(())
}
