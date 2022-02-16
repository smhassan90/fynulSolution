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
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author Syed Muhammad Hassan
 * 1st February, 2022
 */

@Controller
public class Sales {

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/salesUpdate", method = RequestMethod.GET,params={"transaction_date","oracleDate"})
    @ResponseBody
    public String salesUpdate(String transaction_date, String oracleDate){

        String maxVIDSALDaily = getMaxVID("SAL_DAILY_DATA_HIS_COPY WHERE transaction_date like '%"+oracleDate + "%'");
        String maxVIDSDMonthly = getMaxVID("SD_MONTHLY_FINAL_DATA WHERE transaction_date like '%"+oracleDate + "%'");

        if(maxVIDSALDaily.equals(maxVIDSDMonthly)){
            return "Already Updated";
        }

        String minDate = getMinTransactionDate(maxVIDSALDaily);
        String maxDate = getMaxTransactionDate(maxVIDSALDaily);
        ArrayList<Object> rows = new ArrayList<>();
        String strquery = "";
        String strDestID ="";

        rows = getDestinationRecords(minDate,maxDate);

        String deleteQuery = "";
        String copyQuery = "";
        if(rows!=null && rows.size()>0){
            strDestID = getDestinationVID(minDate, maxDate);

            deleteQuery = "delete from SD_MONTHLY_FINAL_DATA where vid='" + strDestID + "'";
        }

        copyQuery += "insert into  SD_MONTHLY_FINAL_DATA(select * from SAL_DAILY_DATA_HIS_COPY where vid='" + maxVIDSALDaily + "')";

        if(strDestID.equals(maxVIDSALDaily)){

        }else{
            if(deleteQuery!=""){
                HibernateUtil.executeQueryOracle(deleteQuery);

            }
            HibernateUtil.executeQueryOracle(copyQuery);
        }

        String transactionDate = HibernateUtil.getSingleStringOracle("SELECT MAX(TRANSACTION_DATE) FROM SD_MONTHLY_FINAL_DATA");
        // Run sales on sale_detail_temp MYSQL for HAWK
        //Delete from sale_detail_temp mySQL
        boolean isSuccessful = HibernateUtil.executeQueryMySQL("DELETE FROM SALE_DETAIL_TEMP WHERE transaction_date like '%"+transaction_date+"%'");
        if(isSuccessful){
            String dateForSD = "";

            Date date  = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(transactionDate);
                dateForSD = new SimpleDateFormat("MMM-yy").format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SalesDistribution salesDistribution = new SalesDistribution();
            String millis = salesDistribution.distributeSales(dateForSD.toUpperCase());
            // Copy sales on sale_detail_temp Oracle for IKON
            HibernateUtil.executeQueryOracle("DELETE FROM SALE_DETAIL_TEMP WHERE transaction_date like '%"+dateForSD+"%'");
            Transfer transfer = new Transfer();
            transfer.sale(transaction_date);


            if(transactionDate!=null && transactionDate!="") {
                String body = "Daily Sales Data is now available till " + transactionDate.split(" ")[0] + " in HAWK and IKON.\n It is a System Generated Email.";
                sendEmail(body);
            }
        }






        return "Done";
    }


    private void sendEmail(String body){
        // Sender's email ID needs to be mentioned
        String from = "support.it@greenstar.org.pk";
        final String username = "support.it@greenstar.org.pk";//change accordingly
        final String password = "Pass@word12";//change accordinglygi

        // Assuming you are sending email through relay.jangosmtp.net
        final String host = "smtp.office365.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        InternetAddress[] ccAddress = new InternetAddress[6];

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        PasswordAuthentication pswAuth = new PasswordAuthentication(username, password);
                        return pswAuth;
                    }
                });

        try {
            ccAddress[0] = new InternetAddress("sajidali@greenstar.org.pk");
            ccAddress[1] = new InternetAddress("umeriftikhar@greenstar.org.pk");
            ccAddress[2] = new InternetAddress("masif@greenstar.org.pk");
            ccAddress[3] = new InternetAddress("hasnain.ali@greenstar.org.pk");
            ccAddress[4] = new InternetAddress("mtafseer@greenstar.org.pk");
            ccAddress[5] = new InternetAddress("syedhassan@greenstar.org.pk");
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
            message.setSubject("Daily Sales Data");

            // put everything together
            message.setText(body);

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

    private String getDestinationVID(String minDate, String maxDate){

        String maxVID = "";
        String query = "select max(vid) from SD_MONTHLY_FINAL_DATA where NATURE IS NULL AND TRANSACTION_DATE BETWEEN TO_DATE('" + getOnlyDate(minDate) + "','YYYY-MM-DD') AND TO_DATE('" + getOnlyDate(maxDate) + "','YYYY-MM-DD')";

        maxVID = HibernateUtil.getSingleStringOracle(query);

        return maxVID;
    }

    private String getOnlyDate(String date){
        if(date!="" && date.contains(" ")) {
            String[] onlyDate;
            onlyDate = date.split(" ");
            return onlyDate[0];
        }else{
            return "";
        }
    }

    private ArrayList<Object> getDestinationRecords(String minDate, String maxDate){
        ArrayList<Object> rows = new ArrayList<>();
        String query = "select distinct vid from SD_MONTHLY_FINAL_DATA where TRANSACTION_DATE BETWEEN TO_DATE('" + getOnlyDate(minDate) + "','YYYY-MM-DD') AND TO_DATE('" + getOnlyDate(maxDate) + "','YYYY-MM-DD')";

        rows = HibernateUtil.getDBObjectsFromSQLQueryOracle(query);

        return rows;
    }

    private String getMaxVID(String tableName){
        String vid =  HibernateUtil.getSingleStringOracle("SELECT MAX(VID) FROM "+tableName);

        return vid;
    }

    private String getMinTransactionDate(String maxVID){
        String date = "";
        String query = "select min(transaction_date) as mindate from SAL_DAILY_DATA_HIS_COPY where vid='" + maxVID + "'";

        date = HibernateUtil.getSingleStringOracle(query);

        return date;
    }

    private String getMaxTransactionDate(String maxVID){
        String date = "";
        String query = "select max(transaction_date) as mindate from SAL_DAILY_DATA_HIS_COPY where vid='" + maxVID + "'";

        date = HibernateUtil.getSingleStringOracle(query);

        return date;
    }
}
