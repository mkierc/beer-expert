import gnu.prolog.database.PrologTextLoaderError;
import gnu.prolog.io.TermWriter;
import gnu.prolog.term.AtomTerm;
import gnu.prolog.term.CompoundTerm;
import gnu.prolog.term.Term;
import gnu.prolog.term.VariableTerm;
import gnu.prolog.vm.*;

import java.util.List;

public class BeerParser {
    private static boolean issetup = false;
    private static Environment env;
    private static Interpreter interpreter;

    private BeerParser() {
    }

    public static void main(String[] args) {
        try {
            // Get the question to ask
            String answer = generateAnswer();
            // Print out the question
            System.out.print(answer);

            // Get a reader to read in the generateAnswer
            /*
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String generateAnswer = reader.readLine();
            */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateAnswer() throws PrologException, NoAnswerException {
        setup();

        // // Construct the question.
        // Create variable terms so that we can pull the answers out later
        VariableTerm xTerm = new VariableTerm("X");
        //VariableTerm answerTerm = new VariableTerm("Answer");
        // Create the arguments to the compound term which is the question
        //Term[] args = {new IntegerTerm(limit), new IntegerTerm(length), listTerm, answerTerm};
        Term[] args = {xTerm};
        //VariableTerm beerStyleTerm = new VariableTerm("beerstyle");
        // Construct the question
        CompoundTerm beerStyleTerm = new CompoundTerm(AtomTerm.get("beerstyle"), args);

        synchronized (interpreter) {
            // Print out any errors
            debug();

            // Execute the goal and return the return code.
            int rc = interpreter.runOnce(beerStyleTerm);

            // HERE BE MAGIC:
            if (rc == PrologCode.SUCCESS || rc == PrologCode.SUCCESS_LAST) {
                // Create the generateAnswer
                String answer;

                // Get hold of the actual Terms which the variable terms point to
                Term defXTerm = xTerm.dereference();
                //Term value = answerTerm.dereference();
                // Check it is valid
                if (defXTerm != null) {
                    if (defXTerm instanceof CompoundTerm) {
                        CompoundTerm cList = (CompoundTerm) defXTerm;
                        if (cList.tag == TermConstants.listTag)// it is a list
                        {// Turn it into a string to use.
                            answer = TermWriter.toString(defXTerm);
                        } else {
                            throw new NoAnswerException("List is not a list but is a CompoundTerm: " + defXTerm);
                        }
                    } else {
                        throw new NoAnswerException("List is not a list: " + defXTerm);
                    }
                } else {
                    throw new NoAnswerException("List null when it should not be null");
                }
                return answer;
            } else {
                throw new NoAnswerException("Goal failed");
            }
        }
    }

    private synchronized static void setup() {
        if (issetup) {
            return;// don't setup more than once
        }
        // Construct the environment
        env = new Environment();
        // get the filename relative to the class file
        env.ensureLoaded(AtomTerm.get(BeerParser.class.getResource("piwa.pg").getFile()));
        // Get the interpreter.
        interpreter = env.createInterpreter();
        // Run the initialization
        env.runInitialization(interpreter);
        // So that we don't repeat ourselves
        issetup = true;
    }

    private static void debug() {
        List<PrologTextLoaderError> errors = env.getLoadingErrors();
        for (PrologTextLoaderError error : errors) {
            error.printStackTrace();
        }
    }
}
