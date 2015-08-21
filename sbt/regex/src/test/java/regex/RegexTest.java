package regex;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;

import util.regex.Matcher;
import util.regex.Pattern;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegexTest {
    private static final Logger log = LoggerFactory.getLogger(RegexTest.class);

    private static final String EMPTY_STRING = "";
    /** Pattern to remove e-mails and urls from the text. */
    public static final Pattern URLS = Pattern.compile(
              "(?:[\\p{Nd}a-z\\._\\-]+@[\\p{Nd}a-z\\._\\-]+)|" //email
            + "(?:(?:(?:ht|f)tp(?:s?)\\:\\/\\/|~\\/|\\/)?" //protocol
            + "(?:\\w+:\\w+@)?" //Username:Password@
            + "(?:(?:(?:[-\\w]+\\.)+" //Subdomains
            + "(?:com|org|net|gov|mil|biz|info|mobi|name|aero|jobs|museum|travel|[a-z]{2}))|"//(?#Top level domain)
            + "(?:(?:\\d{1,3}\\.){3}\\d{1,3}))" //Top level domain as IP address
            + "(?::[\\d]{1,5})?" //Port
            + "(?:(?:(?:\\/(?:[-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" //Directories
            + "(?:(?:\\?(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?(?:[-\\w~!$+|.,*:=]|"
            + "%[a-f\\d]{2})*)(?:&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?(?:[-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*" //Query
            + "(?:#(?:[-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?)" //Anchor
            , Pattern.CASE_INSENSITIVE
    );

    @Test
    public void testRegex() throws Exception {
        //filterText("Amazon fire tv issue hence transferring.Reason For Contact: Account Assistance &#62; Prime &#62; Subscription / Eligibility &#62; C. Education - Info Requested");

        filterText("Hi There Thanks for your e-mail I have an Amazon fire stick and now that I am a prime member I look forward to enjoying this service. However, could you explain to me when I try to watch a programme via my fire stick on Prime it does not seem to recognise that Iam now a member.The programme options seem to be a 30 day trial. Could you please explain Thanks - Frank  On Tuesday, 7 July 2015, Amazon Prime <prime@amazon.co.uk> wrote:  >            [image: Amazon Prime] > <https://www.amazon.co.uk/gp/r.html?C=12ULP8G7LW136&K=ACTD5YW59GAL7&R=114TKA4YDJULS&T=C&U=http%3A%2F%2Fwww.amazon.co.uk%2Fgp%2Fhomepage.html%2Fref%3Dpe_983031_72755631_pe_p_logo&A=Q2DVUZGJZZREPVIU0VLTOAZNC9MA&H=9YUAQJGCBEEPMMLO5EILRQLSZFGA&ref_=pe_983031_72755631_pe_p_logo>      Your > Amazon.co.uk > <https://www.amazon.co.uk/gp/r.html?C=12ULP8G7LW136&K=ACTD5YW59GAL7&R=114TKA4YDJULS&T=C&U=http%3A%2F%2Fwww.amazon.co.uk%2Fgp%2Fyourstore%2Fhome%2Fref%3Dpe_983031_72755631_pe_p_ys&A=VTMXHMFFOHPPBUHHG15PMRH0B8UA&H=6LAEYJTOA5AMTATGEHIHGAIPFVQA&ref_=pe_983031_72755631_pe_p_ys>        Today's > Deals > <https://www.amazon.co.uk/gp/r.html?C=12ULP8G7LW136&K=ACTD5YW59GAL7&R=114TKA4YDJULS&T=C&U=http%3A%2F%2Fwww.amazon.co.uk%2Fb%2Fref%3Dpe_983031_72755631_pe_p_goldbox%3F_encoding%3DUTF8%26node%3D350613011&A=BOGUAKMUH18G4Q6BBW7JYBV2ADGA&H=K3PF6X66ZPOQKI3WCUHHAK9TOCAA&ref_=pe_983031_72755631_pe_p_goldbox>      Shop > All Departments > <https://www.amazon.co.uk/gp/r.html?C=12ULP8G7LW136&K=ACTD5YW59GAL7&R=114TKA4YDJULS&T=C&U=http%3A%2F%2Fwww.amazon.co.uk%2Fgp%2Fsite-directory%2Fref%3Dpe_983031_72755631_pe_p_all&A=XXPSWT1V3EBFZPJVTOKRYMHOJZ0A&H=SWAKL6YM6PV1TKHUJTEXLMBCSDKA&ref_=pe_983031_72755631_pe_p_all> > [image: Amazon Prime] >      Hello Frank O'kane, > > Start taking advantage of your Amazon Prime membership today. Now that > you're a member, you can get your orders delivered fast with unlimited One-Day > Delivery > <https://www.amazon.co.uk/gp/r.html?C=12ULP8G7LW136&K=ACTD5YW59GAL7&R=114TKA4YDJULS&T=C&U=http%3A%2F%2Fwww.amazon.co.uk%2Fgp%2Fhelp%2Fcustomer%2Fdisplay.html%2Fref%3Dpe_983031_72755631_pe_p_201015970_faq_one%3FnodeId%3D200182420%255d&A=AXS8DBCAXAIR4RJAD6Q21A8XA34A&H=A4VSGZQ8XASVUVUSR5JGSHL7SSEA&ref_=pe_983031_72755631_pe_p_201015970_faq_one> > , watch thousands of movies and TV shows > <https://www.amazon.co.uk/gp/r.html?C=12ULP8G7LW136&K=ACTD5YW59GAL7&R=114TKA4YDJULS&T=C&U=http%3A%2F%2Fwww.amazon.co.uk%2FPrime-Instant-Video%2Fb%3Fnode%3D3280626031%26ref_%3Dpe_983031_72755631&A=JDSAVOE5A8USL4E3MBZHB2A89FUA&H=IHAX8D3XJYXDRLSYAIQYLRHYEGKA&ref_=pe_983031_72755631> > on your TV, PC or Kindle Fire, enjoy secure unlimited photo storage > <https://www.amazon.co.uk/gp/r.html?C=12ULP8G7LW136&K=ACTD5YW59GAL7&R=114TKA4YDJULS&T=C&U=http%3A%2F%2Fwww.amazon.co.uk%2Fclouddrive%2Fprimephotos%3Fref_%3Dpe_983031_72755631&A=YCKNE21ZWI776CVERBYYIKHHH4QA&H=FLAB5ITP1ABKKFXMDMUY02SLSLQA&ref_=pe_983031_72755631> > with anywhere access, and borrow a Kindle book each month at no extra cost > from the Kindle Owners' Lending Library > <https://www.amazon.co.uk/gp/r.html?C=12ULP8G7LW136&K=ACTD5YW59GAL7&R=114TKA4YDJULS&T=C&U=http%3A%2F%2Fwww.amazon.co.uk%2Fgp%2Ffeature.html%2Fref%3Dpe_983031_72755631_sa_menu_kds5%3Fie%3DUTF8%26docId%3D1000659983&A=RRFGHKRQATTTM8OFSGH7QZYWMEKA&H=EDZ0VFEV9AZSBE37ZT4BUE1ODOIA&ref_=pe_983031_72755631_sa_menu_kds5>. > It's all included. > > Happy shopping, watching and reading. >           *Movies included in your Prime membership* > <https://www.amazon.co.uk/gp/r.html?C=12ULP8G7LW136&K=ACTD5YW59GAL7&R=114TKA4YDJULS&T=C&U=http%3A%2F%2Fwww.amazon.co.uk%2Fs%2F%3Frh%3Di%253Ainstant-video%252Cn%253A3010085031%252Cn%253A3356010031%26sort%3Dpopularity-rank%26rw_html_to_wsrp%3D1%26pf_rd_m%3DA3P5ROKL5A1OLE%26pf_rd_s%3Dcenter-9%26pf_rd_r%3D1AYF9DXH7FYNGPWV2579%26pf_rd_t%3D101%26pf_rd_p%3D0%26pf_rd_i%3D3280626031%26pf_rd_p%3D472115707%26pf_rd_s%3Dcenter-8%26pf_rd_t%3D101%26pf_rd_i%3D3280626031%26pf_rd_m%3DA3P5ROKL5A1OLE%26pf_rd_r%3D1AYF9DXH7FYNGPWV2579%26ref%3Dwe_mo_s9_t");                
        assertTrue(true);
    }

    private String filterText(String text) {
        Matcher matcher = URLS.matcher(text);
	return matcher.replaceAll(EMPTY_STRING);
    }
}
