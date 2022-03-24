package com.fynuls.controllers.greensales;

import com.fynuls.utils.HibernateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author Syed Muhammad Hassan
 * 17th February, 2022
 */

@Controller
public class SalesReport {
    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getUnderperformedEmployees", method = RequestMethod.GET,params={"reportingMonth"})
    @ResponseBody
    public String getUnderperformedEmployees(String reportingMonth){
        String query = "SELECT sale_detail_temp.POSITION_CODE,e.id,e.NAME, d.NAME as 'Designation', e.NAME as 'supervisor name', sale_detail_temp.region as 'region', sale_detail_temp.TEAM as 'team', SUM(TP_SALE_VALUE) AS 'ACHIEVED'\n" +
                " , SUM(t.TGT_TP_VALUE) AS 'TGT_TP_VALUE', ((SUM(TP_SALE_VALUE)/SUM(t.TGT_TP_VALUE))*100) as 'perc'\n" +
                "FROM (SELECT sdt.EMPLOYEEID AS 'EMPLOYEEID', sdt.TEAM AS 'TEAM', sdt.REGION AS 'REGION', sdt.POSITION_CODE AS 'POSITION_CODE', SUM(TP_SALE_VALUE) AS 'TP_SALE_VALUE'\n" +
                "      FROM `sale_detail_temp` sdt\n" +
                "WHERE reportingmonth='January,2022' \n" +
                "GROUP BY sdt.EMPLOYEEID, sdt.TEAM, sdt.REGION, sdt.POSITION_CODE) as sale_detail_temp\n" +
                "INNER JOIN base_employee E ON E.ID = sale_detail_temp.EMPLOYEEID\n" +
                "INNER JOIN base_designation d on d.ID = e.DESIGNATION_ID\n" +
                "INNER JOIN (SELECT POSITION_CODE,MONTH, SUM(TGT_TP_VALUE) AS 'TGT_TP_VALUE' FROM BASE_TARGET WHERE MONTH = '2022-01-01' GROUP BY POSITION_CODE,MONTH)  t on t.POSITION_CODE = sale_detail_temp.POSITION_CODE\n" +
                "WHERE d.ID IN ('0001','0002') \n" +
                "group by sale_detail_temp.POSITION_CODE,e.id,e.NAME, d.NAME , e.NAME, sale_detail_temp.region, sale_detail_temp.TEAM\n" +
                "HAVING PERC<60\n" +
                "ORDER BY PERC ASC";

        ArrayList<Object> data = null;
        data = HibernateUtil.getDBObjectsFromSQLQuery(query);
        String html = "";
        String text = "<p><b>Dear All,</b></p><p class='.bg-danger'> Below is the list of SPOs and MIOs who have achieved below 60% MTD in the month of January.</p><p><b>Note: The values are in Trade Price</b></p>";
        if(data!=null){
            html +="<html><head><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\"></head><body>";
            html+="<table class=\"table table-striped\">";
            html+="<thead>";
            html+=text;
            html+="<tr>";
            html+="<th scope=\"col\">ID</th>";
            html+="<th scope=\"col\">Name</th>";
            html+="<th scope=\"col\">Designation</th>";
            html+="<th scope=\"col\">Region</th>";
            html+="<th scope=\"col\">Team</th>";
            html+="<th scope=\"col\">Achieved</th>";
            html+="<th scope=\"col\">Target</th>";
            html+="<th scope=\"col\">PERCENTAGE</th>";
            html+="</tr>";
            html+="</thead>";
            html+="<tbody>";
            for(Object obj : data){
                Object[] objArray = (Object[]) obj;
                html+="<tr class='table-danger' scope=\"row\">";
                html+="<td>"+objArray[1].toString()+"</td>";
                html+="<td>"+objArray[2].toString()+"</td>";
                html+="<td>"+objArray[3].toString()+"</td>";
                html+="<td>"+objArray[5].toString()+"</td>";
                html+="<td>"+objArray[6].toString()+"</td>";
                html+="<td>"+Performance.currencyFormat(new BigDecimal(objArray[7].toString()))+"</td>";
                html+="<td>"+Performance.currencyFormat(new BigDecimal(objArray[8].toString()))+"</td>";
                html+="<td>"+Performance.currencyFormat(new BigDecimal(objArray[9].toString()))+"</td>";
                html+="</tr>";

            }
            html+="</tbody>";
            html+="</table>";
            html+="</body>";
        }


//        sendEmail(html);
        return html;
    }
    private void sendEmail(String body){

        // Sender's email ID needs to be mentioned
//        String from = "support.it@greenstar.org.pk";
//        final String username = "support.it@greenstar.org.pk";//change accordingly
        String from = "greenstarsocial.1@gmail.com";
        final String password = "greenstarsocial123@";//change accordingly

        // Assuming you are sending email through relay.jangosmtp.net
        //final String host = "smtp.office365.com";
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        //    props.put("mail.smtp.port", "587");
        props.put("mail.smtp.port", "465");
        InternetAddress[] ccAddress = new InternetAddress[2];

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        PasswordAuthentication pswAuth = new PasswordAuthentication(from, password);
                        return pswAuth;
                    }
                });

        try {
            ccAddress[0] = new InternetAddress("hasnain.ali@greenstar.org.pk");
            ccAddress[1] = new InternetAddress("syedhassan@greenstar.org.pk");
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.

            String toAddress = "shahzaibsattar@greenstar.org.pk";
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toAddress));

            message.setRecipients(Message.RecipientType.CC, ccAddress);
            // Set Subject: header field
            message.setSubject("Monthly Sales Report");

            // put everything together
//            message.setText(body);
            message.setContent(body
                    , "text/html; charset=utf-8");
            // Send message
            // TO-DO: Uncomment this
          Transport.send(message);

        } catch (MessagingException e) {
            int i = 0;
        }catch(Exception ex){
            int i =0;
        }
        int ss = 9;
    }
}
