package com.rain.fabricdemo.ledger;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Transaction;
import org.hyperledger.fabric.gateway.impl.GatewayImpl;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;

public class QueryLedger {
    public static int TRANSACT_SAVINGS_SUCCESS_NUM=0;
    public static int TRANSACT_SAVINGS_FAIL_NUM=0;
    public static int DEPOSIT_CHECKING_SUCCESS_NUM=0;
    public static int DEPOSIT_CHECKING_FAIL_NUM=0;
    public static int SEND_PAYMENT_SUCCESS_NUM=0;
    public static int SEND_PAYMENT_FAIL_NUM=0;
    public static int WRITE_CHECK_SUCCESS_NUM=0;
    public static int WRITE_CHECK_FAIL_NUM=0;
    public static int BALANCE_SUCCESS_NUM=0;
    public static int BALANCE_FAIL_NUM=0;
    public static int AMALGAMATE_SUCCESS_NUM=0;
    public static int AMALGAMATE_FAIL_NUM=0;
    public static void queryInPeers(GatewayImpl gateway, Channel channel, Collection<Peer> endorserSet, String account) {
        try {

            QueryByChaincodeRequest queryByChaincodeRequest = gateway.getClient().newQueryProposalRequest();
            queryByChaincodeRequest.setChaincodeName("mycc");
            queryByChaincodeRequest.setFcn("query");
            queryByChaincodeRequest.setArgs(account);

            Collection<ProposalResponse> proposalResponses = channel.queryByChaincode(queryByChaincodeRequest, endorserSet);
            for (ProposalResponse prores: proposalResponses) {
                String result = prores.getProposalResponse().getResponse().getPayload().toStringUtf8();
                System.out.printf("%s: %s is %s\n", prores.getPeer().getName(), account, result);
            }
        } catch (Exception e) {
            System.out.println("queryByPeer Error");
            e.printStackTrace();
        }
    }

    public static void initInPeers(Contract contract, Collection<Peer> endorserSet, String account) {
        try {

            Transaction transaction = contract.createTransaction("Init");

            transaction.setEndorsingPeers(endorserSet);

            transaction.submit(account, "100", account+"test", "100");

        } catch (Exception e) {
            System.out.println("Invoke Error");
            e.printStackTrace();

        }
    }
    public static void invokeInPeers(Contract contract, Collection<Peer> endorserSet, String from,String to) {
        try {
            //byte[] invokeResult = contract.createTransaction("invoke")
            //        .setEndorsingPeers(endorserSet)
            //        .submit(from, to, "1");
            byte[] invokeResult = contract.createTransaction("query")
                    .setEndorsingPeers(endorserSet)
                    .submit(from);

            System.out.println(new String(invokeResult, StandardCharsets.UTF_8));

        } catch (Exception e) {
            System.out.println("Invoke Error");
            e.printStackTrace();

        }
    }
    public static void TRANSACT_SAVINGS(Contract contract, Collection<Peer> endorserSet, String from) {
        try {
            byte[] invokeResult = contract.createTransaction("TRANSACT_SAVINGS")
                    .setEndorsingPeers(endorserSet)
                    .submit(from, "1");
            //System.out.println("TRANSACT_SAVINGS SUCCESS with "+from);

            TRANSACT_SAVINGS_SUCCESS_NUM++;
        } catch (Exception e) {
            //System.out.println("TRANSACT_SAVINGS Error");
            TRANSACT_SAVINGS_FAIL_NUM++;
            //e.printStackTrace();

        }
    }
    public static void DEPOSIT_CHECKING(Contract contract, Collection<Peer> endorserSet, String from) {
        try {
            byte[] invokeResult = contract.createTransaction("DEPOSIT_CHECKING")
                    .setEndorsingPeers(endorserSet)
                    .submit(from, "10");
            //System.out.println("DEPOSIT_CHECKING SUCCESS with "+from);

            DEPOSIT_CHECKING_SUCCESS_NUM++;
        } catch (Exception e) {
            //System.out.println("DEPOSIT_CHECKING Error");
            DEPOSIT_CHECKING_FAIL_NUM++;
            //e.printStackTrace();

        }
    }
    public static void SEND_PAYMENT(Contract contract, Collection<Peer> endorserSet, String from, String to) {
        try {
            byte[] invokeResult = contract.createTransaction("SEND_PAYMENT")
                    .setEndorsingPeers(endorserSet)
                    .submit(from, to, "100");
            //System.out.println("SEND_PAYMENT SUCCESS from:"+from+" to:"+to);

            SEND_PAYMENT_SUCCESS_NUM++;
        } catch (Exception e) {
            //System.out.println("SEND_PAYMENT Error");
            SEND_PAYMENT_FAIL_NUM++;
            //e.printStackTrace();

        }
    }
    public static void WRITE_CHECK(Contract contract, Collection<Peer> endorserSet, String from) {
        try {
            byte[] invokeResult = contract.createTransaction("WRITE_CHECK")
                    .setEndorsingPeers(endorserSet)
                    .submit(from, "1000");
            //System.out.println("WRITE_CHECK SUCCESS with "+from);

            WRITE_CHECK_SUCCESS_NUM++;
        } catch (Exception e) {
            //System.out.println("WRITE_CHECK Error");
            WRITE_CHECK_FAIL_NUM++;
            //e.printStackTrace();

        }
    }
    public static void BALANCE(Contract contract, Collection<Peer> endorserSet, String from) {
        try {
            byte[] invokeResult = contract.createTransaction("BALANCE")
                    .setEndorsingPeers(endorserSet)
                    .submit(from);
            //System.out.print("BALANCE SUCCESS with "+from+" information as follows:");
            //System.out.println(new String(invokeResult, StandardCharsets.UTF_8));
            BALANCE_SUCCESS_NUM++;
        } catch (Exception e) {
            //System.out.println("BALANCE Error");
            BALANCE_FAIL_NUM++;
            //e.printStackTrace();

        }
    }
    public static void AMALGAMATE(Contract contract, Collection<Peer> endorserSet, String from, String to) {
        try {
            byte[] invokeResult = contract.createTransaction("AMALGAMATE")
                    .setEndorsingPeers(endorserSet)
                    .submit(from, to);
            //System.out.println("AMALGAMATE SUCCESS from:"+from+" to:"+to);

            AMALGAMATE_SUCCESS_NUM++;
        } catch (Exception e) {
            //System.out.println("AMALGAMATE Error");
            AMALGAMATE_FAIL_NUM++;
            //e.printStackTrace();

        }
    }
}
