package com.fynuls.dao;

import com.fynuls.controllers.greensales.Codes;
import com.fynuls.controllers.greensales.Performance;
import com.fynuls.dal.Packet;
import com.fynuls.dal.SyncObjectSS;
import com.fynuls.entity.base.Employee;
import com.fynuls.entity.base.PRDGroupOn;
import com.fynuls.entity.base.Universe;
import com.fynuls.entity.sale.*;
import com.fynuls.utils.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Syed Muhammad Hassan
 * 24/06/2019
 */
public class SyncDAO {
    final static Logger LOG = Logger.getLogger(SyncDAO.class);

    /*
    Get  Staff
     */
    public String getStaffName(String code){
        String query =  "from Employee WHERE ID='"+code+"'";
        List<Employee> data = new ArrayList<>();
        data = (List<Employee>) HibernateUtil.getDBObjects(query);
        if(data!=null && data.size()>0){
            return data.get(0).getNAME();
        }else{
            return "";
        }
    }

    /*
        Fetch all depots which are tagged to this person (Code).
     */
    public List<Depot> getDepots(String code){
        List<Depot> sdDepots = new ArrayList<Depot>();
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            SQLQuery query = session.createSQLQuery("SELECT DISTINCT(sd_depo.DEPO_CODE) ,sd_depo.DEPO_NAME FROM sd_depo" +
                    " JOIN sd_depo_staff ds on ds.DEPO_ID=sd_depo.DEPO_CODE WHERE ds.STAFF_CODE='"+code+"'")
                    .addEntity(Depot.class);
            sdDepots = query.list();
        }catch(Exception e){
            LOG.error(e);
        }finally {
            session.close();
        }
        return sdDepots;
    }

    /*
    Fetch All Towns which are tagged to person.
     */
    public List<Town> getTowns(String code){
        List<Town> sdTowns = new ArrayList<Town>();
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            SQLQuery query = session.createSQLQuery("SELECT DISTINCT(t.TOWN_ID), t.TOWN_NAME FROM SD_TOWN t " +
                    "INNER JOIN SD_TOWN_STAFF ts ON ts.TOWN_ID=t.TOWN_ID WHERE ts.STAFF_CODE='"+code+"'")
                    .addEntity(Town.class);
            sdTowns = query.list();
        }catch (Exception e){
            LOG.error(e);
        }finally {
            session.close();
        }
        return sdTowns;
    }

    /*
    Get cll customer Universe tagged to person who is logged in.
     */
    public List<Universe> getUniverse(String token){
        List<Universe> universeArrayList = new ArrayList<Universe>();
        Performance performance = new Performance();
        String where = performance.getWhereClause(token,true);
        universeArrayList = (List<Universe>) HibernateUtil.getDBObjects("From Universe where "+where);
        return universeArrayList;
    }

    /*
    Fetch the mapping of Depot with region.
     */
    public List<TownDepot> getDepotRegionMapping(String code){
        List<TownDepot> townDepots = new ArrayList<TownDepot>();
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            String sql = "SELECT DISTINCT(td.TOWN_ID), td.DEPO_ID FROM SD_TOWN_DEPO td " +
                    " INNER JOIN SD_DEPOCUST_STAFF DCS ON td.DEPO_ID=SUBSTR(DEPO_CUST,0,3) AND DCS.STAFF_CODE='"+code+"'" +
                    " INNER JOIN SD_TOWN_STAFF TS ON TS.TOWN_ID=td.TOWN_ID AND TS.STAFF_CODE='"+code+"'";
            SQLQuery query = session.createSQLQuery(sql)
                    .addEntity(TownDepot.class);

            townDepots = query.list();
        }catch (Exception e){
            LOG.error(e);
        }finally {
            session.close();
        }

        return townDepots;
    }

    /*
    Fetch the Region and customer mapping.
     */
    public List<TownCustomer> getRegionCustomerMapping(String code){
        List<TownCustomer> townCustomers = new ArrayList<TownCustomer>();
        Session session = null;
        String queryString="SELECT DISTINCT(TC.CUSTOMER_ID), TC.TOWN_ID\n" +
                "   FROM SD_TOWN_CUSTOMER TC\n" +
                "   INNER JOIN SD_TOWN_STAFF TS ON TS.TOWN_ID=TC.TOWN_ID AND TS.STAFF_CODE='"+code+"'" +
                "   INNER JOIN SD_DEPOCUST_STAFF DCS ON DCS.STAFF_CODE='"+code+"' " +
                " AND SUBSTR(DCS.DEPO_CUST,4)=TC.CUSTOMER_ID";
        try{
            session = HibernateUtil.getSessionFactory().openSession();

            SQLQuery query = session.createSQLQuery(queryString)
                    .addEntity(TownCustomer.class);
            townCustomers = query.list();
        }catch(Exception e ){
            LOG.error(e);
        }finally {
            session.close();
        }

        return townCustomers;
    }

    /*
    Fetch the status
     */
    public List<Status> getStatuses(){
        List<Status> statuses = new ArrayList<Status>();
        String queryString="FROM Status";
        statuses = (List<Status>) HibernateUtil.getDBObjects(queryString);

        return statuses;
    }

    /*
    Get SKU
     */
    public List<PRDGroupOn> getSKUGroup(){
        List<PRDGroupOn> prdGroupOns = new ArrayList<PRDGroupOn>();
        prdGroupOns = (List<PRDGroupOn>) HibernateUtil.getDBObjects("From PRDGroupOn");
        return prdGroupOns;
    }

    /*
    Get Work with (RSM/ASM)
     */
    public List<WorkWith> getWorkWiths(String token){
        List<WorkWith> workWiths = new ArrayList<WorkWith>();
        WorkWith workWith = new WorkWith();
        List<String> partners = new Performance().getPartnersArray(token, true);
        if(partners!=null){
            for(int i=0; i<partners.size(); i++){
                workWith = new WorkWith();
                workWith.setId(i+1);
                workWith.setWorkWith(partners.get(i));
                workWiths.add(workWith);
            }
        }

        return workWiths;
    }

    /*
    Insert the data coming from mobile.
     */
    public String insertData(SyncObjectSS syncObject, String staffCode) {

        String status="";
        String message="";
        boolean isSuccessful = true;
        try {
            Packet[] packets = syncObject.getOrderProduct();
            Order tempOrder;
            if(packets!=null){
                for (Packet packet : packets) {
                    tempOrder = new Order();
                    tempOrder = packet.getOrder();
                    tempOrder.setStaffCode(staffCode);
                    if(isSuccessful) {
                        isSuccessful = HibernateUtil.saveOrUpdate(tempOrder);
                    }
                    if(isSuccessful) {
                        isSuccessful = HibernateUtil.saveOrUpdateListMySQL(packet.getProductOrders());
                    }
                }
            }


            List<UnapprovedSDCustomer> unapprovedSDCustomers = syncObject.getUnapprovedSDCustomers();
            List<UnapprovedSDCustomer> unapprovedSDCustomersToSave = new ArrayList<>();
            if(unapprovedSDCustomers!=null){
                for (UnapprovedSDCustomer customer : unapprovedSDCustomers) {
                    customer.setId(0);
                    customer.setStaffCode(staffCode);
                    customer.setDoc(new Date());
                    unapprovedSDCustomersToSave.add(customer);
                }
            }
            if(isSuccessful){
                isSuccessful = HibernateUtil.saveOrUpdateListMySQL(unapprovedSDCustomersToSave);
            }

            List<LeaveEntry> leaveEntries = syncObject.getLeaveEntries();
            List<LeaveEntry> leaveEntriesToSave = new ArrayList<>();
            if(leaveEntries!=null){
                for (LeaveEntry leaveEntry : leaveEntries) {
                    leaveEntry.setId(0);
                    leaveEntry.setStaffCode(staffCode);
                    leaveEntriesToSave.add(leaveEntry);
                }
            }

            if(isSuccessful){
                HibernateUtil.saveOrUpdateListMySQL(leaveEntriesToSave);
            }

            if(isSuccessful){
                status= Codes.ALL_OK;
                message="Successfully Synced";
            }else{
                status=Codes.SOMETHING_WENT_WRONG;
                message="Synced failed. Something went wrong";
            }


        }catch (Exception e){
            status=Codes.SOMETHING_WENT_WRONG;
            message="Synced failed. Something went wrong";

        }

        return status;
    }

}
