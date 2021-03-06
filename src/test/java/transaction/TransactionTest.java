package transaction;

import com.google.protobuf.InvalidProtocolBufferException;
import com.webank.fabric.node.manager.Application;
import com.webank.fabric.node.manager.api.front.FrontRestManager;
import com.webank.fabric.node.manager.common.utils.BeanUtils;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TransactionTest {

    @Autowired
    private FrontRestManager frontRestManager;

    @Test
    public void transactionTest() throws InvalidProtocolBufferException {

        //        TransactionInfo transOnChain = frontRestManager.getTransactionById(channelId, txId);
//
//        Common.Envelope envelopeInfo = transOnChain.getEnvelope();
//        Common.Payload payload = Common.Payload.parseFrom(envelopeInfo.getPayload());
//        FabricTransaction.Transaction transaction =  FabricTransaction.Transaction.parseFrom(payload.getData());
//
//        FabricTransaction.TransactionAction action = transaction.getActionsList().get(0);
//
//        FabricTransaction.ChaincodeActionPayload chaincodeActionPayload = FabricTransaction.ChaincodeActionPayload.parseFrom(action.getPayload());
//        FabricTransaction.ChaincodeEndorsedAction cea = chaincodeActionPayload.getAction();
//
//        FabricTransaction.ProcessedTransaction ft = FabricTransaction.ProcessedTransaction.parseFrom(action.getPayload());
//        ByteString signture = ft.getTransactionEnvelope().getSignature();
//        String signtureSt = signture.toStringUtf8();
//
//        FabricProposalResponse.ProposalResponsePayload prp = FabricProposalResponse.ProposalResponsePayload.parseFrom(chaincodeActionPayload.getAction().getProposalResponsePayload());
//        FabricProposal.ChaincodeAction ca = FabricProposal.ChaincodeAction.parseFrom(prp.getExtension());
//        ByteString result  =  ca.getResults();
//        String message = ca.getResponse().getMessage();
//        int status = ca.getResponse().getStatus();
//        Chaincode.ChaincodeID chaind = ca.getChaincodeId();
//        String chaincodeNam = chaind.getName();
//        String version = chaind.getVersion();
//
//        Rwset.TxReadWriteSet txrws = Rwset.TxReadWriteSet.parseFrom(ca.getResults());
//        TxReadWriteSetInfo txrwsInfo = new TxReadWriteSetInfo(txrws);
//        KvRwset.KVRWSet kvrwSet = txrwsInfo.getNsRwsetInfo(0).getRwset();
//        KvRwset.KVWrite kvWrite = kvrwSet.getWrites(0);
//        String writeVal = kvWrite.getValue().toStringUtf8();
//        System.out.println("writeVal:"+writeVal);
//
//        // FabricTransaction.ChaincodeEndorsedAction mycaction = chaincodeActionPayload.getAction();
////        List<FabricProposalResponse.Endorsement> ecdorList = mycaction.getEndorsementsList();
////        for(FabricProposalResponse.Endorsement ecdor:ecdorList){
////           ByteString str = ecdor.getSignature();
////           String signature = str.toStringUtf8();
////            System.out.println("signature:"+signature);
////        }
//        //        chaincodeActionPayload.getAction().getEndorsementsList().forEach(endorsement -> {
////            endorsement.
////            // This is my current point
////    ???? endorser = ????.parseFrom(endorsement.getEndorser());
////        });
    }

    private BlockInfo blockInfoFromByteArray(byte[] blockByteArray) throws InvalidProtocolBufferException {
        if (null == blockByteArray)
            return null;

        Common.Block block = Common.Block.parseFrom(blockByteArray);
        return BeanUtils.getInstanceByReflection(BlockInfo.class, Arrays.asList(block));
    }

}

