
package com.travelrely.v2.response;

import com.travelrely.v2.net_interface.ChangeChallengeRsp;
import com.travelrely.v2.net_interface.CheckCouponsRsp;
import com.travelrely.v2.net_interface.GetUsrRoamProfileRsp;

public class Response {
    
	/**
	 * 运营商信息
	 * 
	 * @param httpResult
	 * @return
	 */
	public static GetCarriers getCarriers(String httpResult) {
		GetCarriers getGarriers = new GetCarriers();
		getGarriers.setValue(httpResult);

		return getGarriers;
	}

    public static Regist getRegist(String httpResult) {
        Regist regist = new Regist();
        regist.setValue(httpResult);
        return regist;
    }

    public static ChangeChallengeRsp getChangeChallenge(String httpResult) {

        ChangeChallengeRsp changeChallenge = new ChangeChallengeRsp();
        changeChallenge.setValue(httpResult);
        return changeChallenge;
    }

    public static ResetChallenge getResetChallenge(String httpResult) {

        ResetChallenge response = new ResetChallenge();
        response.setValue(httpResult);
        return response;
    }

    /**
     * 在个性化套餐详情点击计算时候触发这个接口的返回
     * 
     * @param httpResult
     * @return
     */
    public static GetCustomized getCustomized(String httpResult) {

        GetCustomized response = new GetCustomized();
        response.setValue(httpResult);
        return response;
    }

    public static NewOrder newOrder(String httpResult) {

        NewOrder response = new NewOrder();
        response.setValue(httpResult);
        return response;
    }

    public static NotifyMe notifyMe(String httpResult) {
        NotifyMe notifyMe = new NotifyMe();
        notifyMe.setValue(httpResult);

        return notifyMe;
    }

    public static GetAppVersion GetAppVersion(String httpResult) {
        GetAppVersion getAppVersion = new GetAppVersion();
        getAppVersion.setValue(httpResult);

        return getAppVersion;
    }

    public static FetchToken FetchToken(String httpResult) {
        FetchToken FetchToken = new FetchToken();
        FetchToken.setValue(httpResult);

        return FetchToken;
    }

    public static SetAdvCat SetAdvCat(String httpResult) {
        SetAdvCat SetAdvCat = new SetAdvCat();
        SetAdvCat.setValue(httpResult);

        return SetAdvCat;
    }

    public static GetCallforwardNumber getCallforwardNumber(String httpResult) {
        GetCallforwardNumber getCallforwardNumber = new GetCallforwardNumber();
        getCallforwardNumber.setValue(httpResult);

        return getCallforwardNumber;
    }
	
	/**
	 * 支付宝充值
	 */
	public static GetRechargeNum getRechargeNum(String httpResult) {
		GetRechargeNum getRechargeNum = new GetRechargeNum();
		getRechargeNum.setValue(httpResult);

		return getRechargeNum;
	}
	
	/**
	 * 优惠券
	 */
	public static CheckCouponsRsp getCheckAoupons(String httpResult) {
		CheckCouponsRsp getAoupons = new CheckCouponsRsp();
		getAoupons.setValue(httpResult);

		return getAoupons;
	}
	
	public static AppendFast getAppendFast(String httpResult) {
	    AppendFast orderAppend = new AppendFast();
        orderAppend.setValue(httpResult);

        return orderAppend;
    }
	
	/**
	 * 续费资费流量数据
	 */
	public static GetReorderPrice getReorderPrice(String httpResult) {
		GetReorderPrice getPrice = new GetReorderPrice();
		getPrice.setValue(httpResult);

		return getPrice;
	}

	/**
	 * 账户余额
	 */
    public static Balance getBalance(String httpResult) {
    	Balance balance = new Balance();
    	balance.setValue(httpResult);

        return balance;
    }
    
	/**
	 * 账户余额支付
	 */
    public static Payment getPayment(String httpResult) {
    	Payment payment = new Payment();
    	payment.setValue(httpResult);

        return payment;
    }
    
    /**
     * 获取消息
     */
	public static GetMessage GetMessage(String httpResult) {
		GetMessage getMessage = new GetMessage();
		getMessage.setValue(httpResult);

		return getMessage;
	}
	
	/**
	 * 群消息成员信息
	 * @param httpResult
	 * @return
	 */
	public static GetGroupMsg getGroupMsg(String httpResult) {
		GetGroupMsg getMsg = new GetGroupMsg();
		getMsg.setValue(httpResult);

		return getMsg;
	}
	
	/**
	 * 新建群
	 */
    public static GetNewGroup getNewGroup(String httpResult) {
        GetNewGroup group = new GetNewGroup();
        group.setValue(httpResult);
        return group;
    }
    
    /**
     * 添加群成员
     */
    public static AddMember getAddMember(String httpResult) {
        AddMember addMember = new AddMember();
        addMember.setValue(httpResult);
        return addMember;
    }
    
    /**
     * 删除群成员
     */
    public static DeleteMember getDeleteMember(String httpResult) {
        DeleteMember deleteMember = new DeleteMember();
        deleteMember.setValue(httpResult);
        return deleteMember;
    }
    
    /**
     * 修改群昵称
     */
    public static UpdateGroupInfo getUpdateGroupInfo(String httpResult) {
        UpdateGroupInfo uNickname = new UpdateGroupInfo();
        uNickname.setValue(httpResult);
        return uNickname;
    }
    
    /**
     * 删除并退出群
     */
    public static QuitGroup qGroup(String httpResult) {
        QuitGroup qGroup = new QuitGroup();
        qGroup.setValue(httpResult);
        return qGroup;
    }
    
    /**
     * 获取行程
     */
    public static TripInfo getTripInfo(String httpResult){
        TripInfo gTripInfo = new TripInfo();
        gTripInfo.setValue(httpResult);
        return gTripInfo;
    }
    
    /**
     * 群消息成员信息
     * @param httpResult
     * @return
     */
    public static GetGroupList getGroupList(String httpResult) {
        GetGroupList getGroupList = new GetGroupList();
        getGroupList.setValue(httpResult);

        return getGroupList;
    }
    
    /**
     * 获取NoRoaming状态
     * @param httpResult
     * @return
     */
    public static GetNoroamingStatus getNoroamingStatus(String httpResult) {
        GetNoroamingStatus getStatus = new GetNoroamingStatus();
        getStatus.setValue(httpResult);

        return getStatus;
    }
    
    /**
     * 获取漫游地信息
     * @param httpResult
     * @return
     */
    public static GetUsrRoamProfileRsp getUserroamProfile(String httpResult) {
        GetUsrRoamProfileRsp getStatus = new GetUsrRoamProfileRsp();
        getStatus.setValue(httpResult);

        return getStatus;
    }
}
    
    
