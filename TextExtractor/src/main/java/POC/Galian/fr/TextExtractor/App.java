package POC.Galian.fr.TextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDNonTerminalField;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void one() throws InvalidPasswordException, IOException{
		try (PDDocument document = PDDocument.load(new File("C:/my.pdf"))) {

            document.getClass();

            if (!document.isEncrypted()) {

                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                PDFTextStripper tStripper = new PDFTextStripper();

                String pdfFileInText = tStripper.getText(document);
                //System.out.println("Text:" + st);

				// split by whitespace
                String lines[] = pdfFileInText.split("\\r?\\n");
                for (String line : lines) {
                    System.out.println(line);
                }
                document.close();

            }

        }
	}
	
	private static void processField(PDField field, String sLevel, String sParent) throws IOException
	{
	        String partialName = field.getPartialName();

	        if (field instanceof PDNonTerminalField)
	        {
	                if (!sParent.equals(field.getPartialName()))
	                {
	                        if (partialName != null)
	                        {
	                                sParent = sParent + "." + partialName;
	                        }
	                }
	                System.out.println(sLevel + sParent);

	                for (PDField child : ((PDNonTerminalField)field).getChildren())
	                {
	                        processField(child, "|  " + sLevel, sParent);
	                }
	        }
	        else
	        {
	            //field has no child. output the value
	                String fieldValue = field.getValueAsString();
	                StringBuilder outputString = new StringBuilder(sLevel);
	                outputString.append(sParent);
	                if (partialName != null)
	                {
	                        outputString.append(".").append(partialName);
	                }
	                outputString.append(" = ").append(fieldValue);
	                outputString.append(",  type=").append(field.getClass().getName());
	                System.out.println(outputString);
	        }
	}
    public static void main( String[] args ) throws IOException
    {
    	
    	PDDocument pdDoc = PDDocument.load(new File("C:/my.pdf"));
    	PDDocumentCatalog pdCatalog = pdDoc.getDocumentCatalog();
    	PDAcroForm pdAcroForm = pdCatalog.getAcroForm();
    	
    	
    	PDField firstNameField = pdAcroForm.getField("«VS35»");
    	PDField lastNameField = pdAcroForm.getField("«VS28»");
    	
    	 List<PDField> fields = pdAcroForm.getFields();
    	    System.out.println(fields.size() + " top-level fields were found on the form");
    	  //inspect field values
    	    for (PDField field : fields)
    	    {
    	            processField(field, "|--", field.getPartialName());
    	    }
    	    
    	    System.out.println("Client "+ firstNameField );
    	    
    	    System.out.println(" Contrat " +lastNameField);
    }
}
