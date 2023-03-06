package tn.esprit.realestate.Services.Appointment.Offer;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.TextField;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.Details;
import tn.esprit.realestate.Entities.Offer;
import tn.esprit.realestate.Repositories.DetailsRepository;
import tn.esprit.realestate.Repositories.OfferRepository;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


@Service
public class PDFGeneratorService {



@Autowired
    private final OfferRepository offerRepository;
     private final DetailsRepository detailsRepository;



     public PDFGeneratorService(OfferRepository offerRepository,
                                DetailsRepository detailsRepository) {
          this.offerRepository = offerRepository;
          this.detailsRepository = detailsRepository;

     }



     public void export(Long id,HttpServletResponse response) throws IOException {


        //create docc


        Document document = new Document(PageSize.A4);

         PdfWriter.getInstance(document,response.getOutputStream());
        FileOutputStream pdfOutputFile = new FileOutputStream("./sample1.pdf");
       Offer offer = offerRepository.findById(id).get();

        String description = "Notre Agence met a votre disposition : "+offer.getDescription().toString();
         String email = "pour plus d'information merci de contacter l'agent responsable: "+offer.getUser().getEmail().toString();
         String description1 = "Composer de : "+offer.getDetails().getDescription().toString()+"\n\n\n\n\n\n\n\n\n\n\n\n";
         String date = "Date limite pour postuler est : "+offer.getDetails().getDeadline();

        final PdfWriter pdfWriter = PdfWriter.getInstance(document, pdfOutputFile);

        /* Rectangle header = new Rectangle(30f, 30f, PageSize.A4.getRight(30f), 140f);
         header.setBorder(Rectangle.BOX);
         header.setBorderColor(Color.RED);
         header.setBorderWidth(1f);
         header.setTop(PageSize.A4.getTop(30f));
         header.setBottom(PageSize.A4.getTop(180f));*/




        /*Rectangle footer= new Rectangle(30f, 30f, PageSize.A4.getRight(30f), 140f);
        footer.setBorder(Rectangle.BOX);
        footer.setBorderColor(Color.BLACK);
        footer.setBorderWidth(2f);
        Font fontTitle1= FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle1.setSize(13);*/

        Font fontTitle= FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(13);




        pdfWriter.setPageEvent(new PdfPageEventHelper()); /*{
             @Override
             public void onEndPage(PdfWriter writer, Document document) {
                  PdfContentByte cb = writer.getDirectContent();
                  //cb.rectangle(header);


                  cb.rectangle(footer);
             }
        });*/

        document.open();

        Image jpg = Image.getInstance("test.png");
        jpg.setAlignment(jpg.ALIGN_LEFT);
        jpg.scaleAbsolute(100,90);




        //jpg.setBorderWidth(50);


        Paragraph paragraph = new Paragraph("OFFER",fontTitle);
    paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(10);
        fontParagraph.setColor(Color.red);
         Paragraph footer1 = new Paragraph(description,fontTitle);
         footer1.setAlignment(Paragraph.ALIGN_BOTTOM);

         Paragraph details = new Paragraph(description1,fontTitle);
         footer1.setAlignment(Paragraph.ALIGN_BOTTOM);

        Paragraph paragraph1=new Paragraph(date,fontParagraph);
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);

         Paragraph paragraph2 = new Paragraph(email,fontTitle);
         paragraph2.setAlignment(Paragraph.ALIGN_BOTTOM);



       // Paragraph footer1 = new Paragraph("aaaaa",fontTitle);
        //footer1.setAlignment(Paragraph.ALIGN_BOTTOM);



        // document.add(header);

        document.add(jpg);
        document.add(paragraph);
        document.add(paragraph1);

        //document.add(footer);
        document.add(footer1);
        document.add(details);
         document.add(paragraph2);
        document.close();
        pdfWriter.close();


    }

}
