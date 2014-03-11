package mpkg;

import net.sourceforge.reb4j.Alternation;
import net.sourceforge.reb4j.Sequence;
import net.sourceforge.reb4j.Literal;
import net.sourceforge.reb4j.CharLiteral;
import net.sourceforge.reb4j.StringLiteral;
import net.sourceforge.reb4j.Quantified;
import net.sourceforge.reb4j.Group;
import net.sourceforge.reb4j.Entity;
import net.sourceforge.reb4j.Expression;
import net.sourceforge.reb4j.charclass.CharClass;
import net.sourceforge.reb4j.charclass.SingleChar;
import net.sourceforge.reb4j.charclass.Negated;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.is;

import static net.sourceforge.reb4j.charclass.CharClass.Perl.*;

import static java.nio.charset.StandardCharsets.*;

public class RegexTest {
    private static final String RES_IN_TXT = "sample_rex_in.txt";
    private static final String RES_OUT_TXT = "sample_rex_out.txt";

    private BufferedReader getR(final String resName) {
            return new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/" + resName), UTF_8));
    }

    @Ignore
    public void testRegexClass() throws Exception {
        Expression e = CharClass.character('d').negated();
        assertThat(e.toString(), is("[^d]"));

        final Matcher m = e.toPattern().matcher("aadabdaaadede");
        assertEquals("dddd", m.replaceAll(""));
    }

    @Test
    public void testRegex() throws Exception {
        // Literal, StringLiteral, CharClass  are both Sequencable, Alternative, Quantifiable
        final CharLiteral und = Literal.literal('_');
        //final CharLiteral hyphen = Literal.literal('-');
        final StringLiteral fatha = Literal.literal("-َ");
        final StringLiteral damma = Literal.literal("-ُ");
        final StringLiteral damma2_1 = Literal.literal("-َِ");
        final StringLiteral damma2_2 = Literal.literal("-َِ");

        final StringLiteral kasra = Literal.literal("-ِ");
        final StringLiteral dammakasra = Literal.literal("-ُِ");

        final CharClass dig = DIGIT;
        final Quantified digp = Quantified.atLeastOnce(dig);

        final Sequence und_digp = Sequence.sequence(und, digp);
        assertThat(und_digp.toString(), is("_\\d+"));

        final Alternation hyphens = Alternation.alternatives(fatha, damma, damma2_1, damma2_2, kasra, dammakasra);
        final Quantified hyphens_opt = Quantified.optional(Group.capture(hyphens));
        assertThat(hyphens_opt.toString(), is("(\\-َ|\\-ُ|\\-َِ|\\-َِ|\\-ِ|\\-ُِ)?"));

        final Sequence grp_end = Sequence.sequence(hyphens_opt, und_digp, Entity.LINE_END);
        assertThat(grp_end.toString(), is("(\\-َ|\\-ُ|\\-َِ|\\-َِ|\\-ِ|\\-ُِ)?_\\d+$"));

        final Pattern pat = grp_end.toPattern();

        try(
            final BufferedReader inR = getR(RES_IN_TXT);
            final BufferedReader outR = getR(RES_OUT_TXT);
        ) {
            while (inR.ready()) {
                final String inS = inR.readLine();
                final String outS = outR.readLine();

                final Matcher matcher = pat.matcher(inS);
                assertEquals(outS, matcher.replaceAll(""));
            }
        }
    }
};
