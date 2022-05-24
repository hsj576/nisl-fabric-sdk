package com.rain.fabricdemo.handler;

import com.rain.fabricdemo.dto.DataItem;
import com.rain.fabricdemo.handler.SimpleStreamDataHandler;
import com.rain.fabricdemo.ledger.ConsistentHashing;
import com.rain.fabricdemo.ledger.QueryLedger;
import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.gateway.impl.GatewayImpl;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Peer;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.LinkedList;

import static com.rain.fabricdemo.test.App.ch;

public class FirstSampleHandler extends SimpleStreamDataHandler {
    private static final Object lock = new Object();
    long searchTimesInOrg1 = 0;
    long searchTimesInOrg2 = 0;

    // connection.json 里应该配置网络信息
    private static final Path NETWORK_CONFIG_PATH = Paths.get("src", "main", "resources", "connection.json");
    // 证书的位置
    private static final Path credentialPath = Paths.get("src", "main", "resources", "organizations",
            "peerOrganizations", "org1.example.com", "users", "Admin@org1.example.com", "msp");

    private X509Certificate certificate;
    private PrivateKey privateKey;
    private Wallet wallet;
    private Gateway gateway;
    private Gateway.Builder builder;
    private Network network;
    private Channel channel;
    private Contract contract;
    private Collection<Peer> peerSet;

    public FirstSampleHandler() {
        try {
            this.certificate = readX509Certificate(credentialPath.resolve(Paths.get("signcerts", "Admin@org1.example.com-cert.pem")));
            this.privateKey = getPrivateKey(credentialPath.resolve(Paths.get("keystore", "priv_sk")));

            // 加载一个钱包，里面有接入网络所需要的identities
            this.wallet = Wallets.newInMemoryWallet();
            // Path walletDir = Paths.get("wallet");
            // Wallet wallet = Wallets.newFileSystemWallet(walletDir);
            this.wallet.put("user", Identities.newX509Identity("Org1MSP", certificate, privateKey));
        } catch (Exception e) {
            System.out.println("读证书错误");
            e.printStackTrace();
        }

        try {
            // 设置连接网络所需要的gateway connection配置信息
            this.builder = Gateway.createBuilder()
                    .identity(this.wallet, "user")
                    .networkConfig(NETWORK_CONFIG_PATH);

            // 创建Gateway连接
            this.gateway = builder.connect();
            // 接入channel
            this.network = gateway.getNetwork("mychannel");

            this.channel = network.getChannel();
            this.contract = network.getContract("smallbank");
            this.peerSet = channel.getPeers();
        } catch (Exception e) {
            System.out.println("连接错误");
            e.printStackTrace();
        }

    }

    private static X509Certificate readX509Certificate(final Path certificatePath) throws IOException, CertificateException {
        try (Reader certificateReader = Files.newBufferedReader(certificatePath, StandardCharsets.UTF_8)) {
            return Identities.readX509Certificate(certificateReader);
        }
    }

    private static PrivateKey getPrivateKey(final Path privateKeyPath) throws IOException, InvalidKeyException {
        try (Reader privateKeyReader = Files.newBufferedReader(privateKeyPath, StandardCharsets.UTF_8)) {
            return Identities.readPrivateKey(privateKeyReader);
        }
    }

    @Override
    public void handle(DataItem in) {
        Collection<Peer> endorserSet = new LinkedList<>();
        //ConsistentHashing ch=new ConsistentHashing();
        // System.out.println("Handle time: " + new Date().toString());
        // if (searchTimesInOrg1 < searchTimesInOrg2) {
        //     searchTimesInOrg1++;
        //     for (Peer peer : this.peerSet) {
        //         if (peer.getName().equals("peer0.org1.example.com")) {
        //             endorserSet.add(peer);
        //             break;
        //         }
        //     }
        // } else {
        //     searchTimesInOrg2++;
        //     for (Peer peer : this.peerSet) {
        //         if (peer.getName().equals("peer0.org2.example.com")) {
        //             endorserSet.add(peer);
        //             break;
        //         }
        //     }
        // }

        // 只添加peer0.org1.com
        for (Peer peer : this.peerSet) {
            if (peer.getName().equals("peer0.org1.example.com") || peer.getName().equals("peer0.org2.example.com")) {
                endorserSet.add(peer);
                //System.out.println(peer.getName());
            }
        }

        switch (in.operation) {
            case "query":
                QueryLedger.queryInPeers((GatewayImpl) gateway, channel, endorserSet, in.from);
                break;
            case "Init":
                QueryLedger.initInPeers(contract, this.peerSet, ""+System.currentTimeMillis());
                break;
            case "Invoke":
                long acc=System.currentTimeMillis();
                String k=String.valueOf(acc);
                String ct=ch.getServer(k);
                this.contract = network.getContract(ct);
                System.out.println("Use Contract:"+ct);
                QueryLedger.invokeInPeers(contract, this.peerSet,in.from,in.to);
                break;
            case "TRANSACT_SAVINGS":
                long acc1=System.currentTimeMillis();
                String k1=String.valueOf(acc1);
                String ct1=ch.getServer(k1);
                this.contract = network.getContract(ct1);
                //System.out.println("Use Contract:"+ct1);
                //System.out.println("TRANSACT_SAVINGS:"+in.from);
                QueryLedger.TRANSACT_SAVINGS(contract, this.peerSet, in.from);
                break;
            case "DEPOSIT_CHECKING":
                long acc2=System.currentTimeMillis();
                String k2=String.valueOf(acc2);
                String ct2=ch.getServer(k2);
                this.contract = network.getContract(ct2);
//                System.out.println("Use Contract:"+ct2);
//                System.out.println("DEPOSIT_CHECKING:"+in.from);
                QueryLedger.DEPOSIT_CHECKING(contract, this.peerSet, in.from);
                break;
            case "SEND_PAYMENT":
                long acc3=System.currentTimeMillis();
                String k3=String.valueOf(acc3);
                String ct3=ch.getServer(k3);
                this.contract = network.getContract(ct3);
//                System.out.println("Use Contract:"+ct3);
//                System.out.println("SEND_PAYMENT:from "+in.from+";to "+in.to);
                QueryLedger.SEND_PAYMENT(contract, this.peerSet, in.from, in.to);
                break;
            case "WRITE_CHECK":
                long acc4=System.currentTimeMillis();
                String k4=String.valueOf(acc4);
                String ct4=ch.getServer(k4);
                this.contract = network.getContract(ct4);
//                System.out.println("Use Contract:"+ct4);
//                System.out.println("WRITE_CHECK: "+in.from);
                QueryLedger.WRITE_CHECK(contract, this.peerSet, in.from);
                break;
            case "BALANCE":
                long acc5=System.currentTimeMillis();
                String k5=String.valueOf(acc5);
                String ct5=ch.getServer(k5);
                this.contract = network.getContract(ct5);
//                System.out.println("Use Contract:"+ct5);
//                System.out.println("BALANCE: "+in.from);
                QueryLedger.BALANCE(contract, this.peerSet, in.from);
                break;
            case "AMALGAMATE":
                long acc6=System.currentTimeMillis();
                String k6=String.valueOf(acc6);
                String ct6=ch.getServer(k6);
                this.contract = network.getContract(ct6);
//                System.out.println("Use Contract:"+ct6);
//                System.out.println("AMALGAMATE:from "+in.from+";to "+in.to);
                QueryLedger.AMALGAMATE(contract, this.peerSet, in.from, in.to);
                break;
            default:
        }
    }
}
