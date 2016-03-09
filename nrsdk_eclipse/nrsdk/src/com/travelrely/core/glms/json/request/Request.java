
package com.travelrely.core.glms.json.request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;

import com.travelrely.app.activity.BaseActivity;
import com.travelrely.core.glms.GetMsg.FetchMessage;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.util.DeviceInfo;
import com.travelrely.core.util.FileUtil;
import com.travelrely.core.util.LOGManager;
import com.travelrely.core.util.TimeUtil;
import com.travelrely.core.util.Utils;
import com.travelrely.model.ContactModel;
import com.travelrely.model.ContactModel.TagNumber;
import com.travelrely.v2.model.Cart;
import com.travelrely.v2.model.Commodity;
import com.travelrely.v2.model.ContactTemp;
import com.travelrely.v2.model.CouponInfo;
import com.travelrely.v2.model.ExpressInfo;
import com.travelrely.v2.model.SimCard;
import com.travelrely.v2.model.UsrSimInfo;
import com.travelrely.v2.response.GroupMsg;
import com.travelrely.v2.response.TraMessage;
import com.travelrely.v2.response.TripInfoList;
import com.travelrely.v2.service.TravelService;

public class Request
{
    public static JSONObject generateBaseJson()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
        	//遵照服务器的要求 version 改为 3.0.0 2/13
            jsonObject.put("version", "3.0.0");
            jsonObject.put("charset", "utf-8");
            jsonObject.put("format", "json");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsonObject;
    }

    // 确定完成注册
    public static final String regist(String nickname, String username,
            String verification_code,
            Activity activity, String gender, String head_portrait)
    {
        String result = "";
        // DeviceInfo deviceInfo = DeviceInfo.getInstance(activity);
        DeviceInfo deviceInfo = DeviceInfo.getInstance(activity
                .getApplication());
        try
        {
            JSONObject regist = generateBaseJson();
            JSONObject data = new JSONObject();
            regist.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", username);
            data.put("country_code", Engine.getInstance().getCC());
            data.put("verification_code", verification_code);
            data.put("gender", gender);
            data.put("first_name", "");
            data.put("last_name", "");
            data.put("nickname", nickname);
            data.put(
                    "user_agent",
                    Utils.getVersion(Engine.getInstance().getContext()
                            .getApplicationContext()));
            data.put("head_portrait", head_portrait);
            initDeviceInfo(data, activity);
            data.put("platform_type", Integer.toString(deviceInfo.platform_type));

            JSONObject geo_pos_info = null;
            // JSONObject geo_pos_info =
            // TravelrelyApplication.app.travelrelyLocation.generateJson();
            data.put("geo_pos_info", geo_pos_info);
            result = regist.toString();
        } catch (Exception e)
        {
        }

        return result;
    }

    private static final void initDeviceInfo(JSONObject data,
            Context activity)
    {
        // DeviceInfo deviceInfo = DeviceInfo.getInstance(activity);
        DeviceInfo deviceInfo = DeviceInfo.getInstance(Engine.getInstance()
                .getContext().getApplicationContext());
        try
        {
            data.put("device_type", deviceInfo.device_type);
            data.put("device_model", deviceInfo.device_model);
            data.put("imei", "");
            data.put("sim_provider_name", "");
            data.put("sim_mcc", deviceInfo.sim_mcc);
            data.put("sim_mnc", deviceInfo.sim_mnc);
            data.put("imsi", "");
        } catch (Exception e)
        {
        }
    }

    public static final String getAppVersion(String username, String version)
    {
        String result = "";
        try
        {
            JSONObject root = generateBaseJson();
            JSONObject data = new JSONObject();
            root.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", username);
            data.put("platform_id", "1");
            data.put("user_agent", version);
            result = root.toString();
            // LOGManager.d(result);
        } catch (Exception e)
        {
        }

        return result;
    }

    // 查询是否旅信用户和获取Token
    public static final String fetchToken(ContactModel contactModel)
    {
        String result = "";
        try
        {
            JSONObject vertifCode = generateBaseJson();
            JSONObject data = new JSONObject();
            vertifCode.put("data", data);

            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            data.put("rf", "2");
            if (contactModel.getPhoneNumList() != null)
            {
                JSONArray jsonArray = new JSONArray();
                data.put("contact_list", jsonArray);
                for (int i = 0; i < contactModel.getPhoneNumList().size(); i++)
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("phone_number", contactModel
                            .getPhoneNumList().get(i).getValue());
                    jsonArray.put(jsonObject);
                }
            }

            result = vertifCode.toString();
        } catch (Exception e)
        {
        }

        return result;
    }
    
    
    // 根据电话号码查询是否旅信用户和获取Token
    public static final String fetchToken1(List<String> PhoneNums)
    {
        String result = "";
        try
        {
            JSONObject vertifCode = generateBaseJson();
            JSONObject data = new JSONObject();
            vertifCode.put("data", data);

            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            data.put("rf", "2");
            if (PhoneNums != null && PhoneNums.size()>0)
            {
                JSONArray jsonArray = new JSONArray();
                data.put("contact_list", jsonArray);
                for (int i = 0; i < PhoneNums.size(); i++)
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("phone_number",PhoneNums.get(i));
                    jsonArray.put(jsonObject);
                }
            }

            result = vertifCode.toString();
        } catch (Exception e)
        {
        }

        return result;
    }

    
    

    /**
     * 获得运营商信息
     * 
     * @param username
     * @param mcc
     * @param link_source
     * @return
     */
    public static final String getCarriers(String username, String mcc,
            int link_source)
    {
        String result = "";
        try
        {
            JSONObject loginRequest = generateBaseJson();
            JSONObject data = new JSONObject();
            loginRequest.put("data", data);
            data.put("link_source", Integer.toString(link_source));
            data.put("username", username);
            data.put("mcc", mcc);
            data.put("packageduration", "5");
            data.put("isindividuation", "0");
            result = loginRequest.toString();
        } catch (Exception e)
        {
        }
        return result;
    }

    /**
     * 支付宝充值
     * 
     * @param linkSource连接源 （0归属地，1漫游）
     * @param userName （用户名）
     * @param currencyUnit （货币单位）
     * @param rechargeAmount （充值金额）
     * @return
     */
    public static final String alipayApp(int linkSource, String userName,
            String currencyUnit, String rechargeAmount)
    {
        String result = "";
        try
        {
            JSONObject verifychallenge = generateBaseJson();
            JSONObject data = new JSONObject();
            verifychallenge.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", userName);
            data.put("currency_unit", currencyUnit);
            data.put("recharge_amount", rechargeAmount);
            result = verifychallenge.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    // 获得用户信息
    public static final String getUserInfo(String username)
    {
        String result = "";
        try
        {
            JSONObject verifyCode = generateBaseJson();
            JSONObject data = new JSONObject();
            verifyCode.put("data", data);

            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", username);

            result = verifyCode.toString();
        } catch (Exception e)
        {
        }

        return result;
    }

    /**
     * @param username
     * @param description 出行国家
     * @param begin_date
     * @param end_date
     * @param mcc
     * @param mnc
     * @param simcardType sim卡类型，对应行程
     * @param crbtId 彩铃编号
     * @param isIndividuation 个性化套餐标识位
     * @param packagePeriod
     * @param packageVoice
     * @param packageData
     * @param currencyUnit
     * @param pricePackage
     * @param idCardNumber
     * @param deliveryMode
     * @param id
     * @param consignee
     * @param country
     * @param state
     * @param city
     * @param address
     * @param phoneNumber
     * @param name
     * @param expressPrice
     * @param favorable_price
     * @param paymentMode
     * @return
     */
    public static final String newOrder(String username)
    {
        // 读取购物车数据
        Cart cart = null;
        try
        {
            String strObject = Engine.getInstance().getCart();
            if (strObject.equals("") == false)
            {
                cart = Cart.deSerialization(strObject);
                // LOGManager.d("read cart=" + cart);
            }
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (cart == null)
        {
            return null;
        }

        double totalMoney = cart.getTotalMoney();

        List<Commodity> commodityItems = cart.getCommodityItems();

        String result = "";
        JSONObject jsonData = null;
        try
        {
            jsonData = generateBaseJson();
            JSONObject data = new JSONObject();
            jsonData.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", username);
            data.put("order_fee", Double.toString(totalMoney));
            data.put("currency_unit", cart.getCurrencyUnit());

            JSONArray orders = new JSONArray();
            data.put("order_list", orders);
            for (Commodity commodity : commodityItems)
            {
                if (commodity.getSelected())
                {
                    SimCard simCard = commodity.getSimCard();
                    JSONObject commodityJSON;
                    for(UsrSimInfo usrSimInfo : simCard.getOperatorPhoneNumbers()){
                        commodityJSON = new JSONObject();
                        commodityJSON.put("order_type", Integer.toString(commodity.getType()));
                        commodityJSON.put("begin_date", simCard.getBeginDate());
                        commodityJSON.put("end_date", simCard.getEndDate());
                        commodityJSON.put("product_id", simCard.getMcc());
                        commodityJSON.put("imei", simCard.getMnc());
                        commodityJSON.put("package_period",simCard.getPackagePeriod());
                        commodityJSON.put("simcard_size", Integer.toString(usrSimInfo.getSimSize()));
                        commodityJSON.put("card_user", usrSimInfo.getPhone());
                        orders.put(commodityJSON);
                    }
                }
            }

//            PersonalInfo personalInfo = cart.getPersonalInfo();
//            JSONObject personal_info = new JSONObject();
//            data.put("personal_info", personal_info);
//            personal_info.put("first_name", personalInfo.getFirst_name());
//            personal_info.put("last_name", personalInfo.getLast_name());
//            personal_info.put("passport_number", personalInfo.getPassport_number());
//            personal_info.put("id_card_number", personalInfo.getId_card_number());
//            personal_info.put("mobile_phone", personalInfo.getMobile_phone());
//            personal_info.put("email", personalInfo.getEmail());
            

            ExpressInfo expressInfo = cart.getExpressInfo();
            JSONObject shipment_info = new JSONObject();
            data.put("shipment_info", shipment_info);
            shipment_info.put("delivery_mode",Integer.toString(expressInfo.getDeliveryMode()));// 递送方式
            shipment_info.put("id", expressInfo.getId());// 快递地址编号
            shipment_info.put("consignee", expressInfo.getConsignee());// 收件人姓名
            shipment_info.put("country", expressInfo.getCountry());// 国家
            shipment_info.put("state", expressInfo.getState());// 州或省
            shipment_info.put("city", expressInfo.getCity());// 城市
            // shipment_info.put("county", expressInfo.getCounty());// 区或县
            shipment_info.put("address", expressInfo.getAddress());// 地址
            shipment_info.put("phone_number", expressInfo.getPhoneNumber());// 电话号码
            shipment_info.put("country", expressInfo.getCountry());// 国家
            // shipment_info.put("name", airport_name);// 机场名字

            CouponInfo couponInfo = cart.getCouponInfo();

            JSONObject other_fee = new JSONObject();
            data.put("other_fee", other_fee);
            // other_fee.put("currency_unit", cart.getCurrencyUnit());// 货币计算单位
            other_fee.put("express_price",
                    Double.toString(expressInfo.getPrice()));
            // other_fee.put("favorable_price",
            // Double.toString(couponInfo.getCouponMoney()));
            // other_fee.put("coupon_code", couponInfo.getCouponCode());
            other_fee.put("coupon_price", couponInfo.getCouponMoney());// 优惠价格

            // question:支付方式
            JSONObject payment = new JSONObject();
            data.put("payment_info", payment);
            payment.put("payment_mode", Integer.toString(cart.getPaymentMode()));// 支付方式
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            result = jsonData.toString();
        }

        return result;
    }

    public static final String checkAoupons(int linkSource, String userName,
            String couponCode)
    {
        String result = "";
        try
        {
            JSONObject verifychallenge = generateBaseJson();
            JSONObject data = new JSONObject();
            verifychallenge.put("data", data);
            data.put("link_source", Integer.toString(linkSource));
            data.put("username", userName);
            data.put("coupon_code", couponCode);
            result = verifychallenge.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public static final String formAppendFastReq(int linkSrc, String usrName,
            String orderId, String tripId,
            String totalFee, double crbtPrice, int days, int payment_mode)
    {
        String result = "";
        try
        {
            JSONObject verifychallenge = generateBaseJson();
            JSONObject data = new JSONObject();
            verifychallenge.put("data", data);
            data.put("link_source", Integer.toString(linkSrc));
            data.put("username", usrName);
            data.put("order_id", orderId);
            data.put("trip_id", tripId);
            data.put("add_trip_days", Integer.toString(days));
            data.put("total_price", totalFee);
            data.put("crbt_price", Double.toString(crbtPrice));
            data.put("payment_mode", Integer.toString(payment_mode));

            result = verifychallenge.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 续订价格
     */
    public static final String reorderPrice(String userName, String mcc,
            String mnc, int reorder_type, String trip_id)
    {
        String result = "";
        try
        {
            JSONObject verifychallenge = generateBaseJson();
            JSONObject data = new JSONObject();
            verifychallenge.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", userName);
            data.put("mcc", mcc);
            data.put("mnc", mnc);
            data.put("reorder_type", Integer.toString(reorder_type));
            data.put("trip_id", trip_id);
            result = verifychallenge.toString();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    // 只查询是否旅信用户
    public static final String fetchToken(List<String> phones)
    {
        String result = "";
        try
        {
            JSONObject vertifCode = generateBaseJson();
            JSONObject data = new JSONObject();
            vertifCode.put("data", data);

            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            data.put("rf", "1");
            if (phones != null)
            {
                JSONArray jsonArray = new JSONArray();
                data.put("contact_list", jsonArray);
                for (int i = 0; i < phones.size(); i++)
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("phone_number", phones.get(i));
                    jsonArray.put(jsonObject);
                }
            }

            result = vertifCode.toString();
        } catch (Exception e)
        {
        }

        return result;
    }

    public static final String fetchToken(HashMap<String, List<ContactTemp>> map)
    {
        String result = "";
        try
        {
            JSONObject vertifCode = generateBaseJson();
            JSONObject data = new JSONObject();
            vertifCode.put("data", data);

            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            data.put("rf", "1");
            if (map != null)
            {
                JSONArray jsonArray = new JSONArray();
                data.put("contact_list", jsonArray);

                Iterator<Entry<String, List<ContactTemp>>> iterator = map.entrySet().iterator();
                while (iterator.hasNext())
                {
                    Entry<String, List<ContactTemp>> entry = (Entry<String, List<ContactTemp>>) iterator
                            .next();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("phone_number", (String) entry.getKey());
                    jsonArray.put(jsonObject);
                }
            }

            result = vertifCode.toString();
        } catch (Exception e)
        {
        }

        return result;
    }

    /**
     * 账户余额
     */
    public static final String getBalance(String username)
    {
        String result = "";
        try
        {
            JSONObject vertifCode = generateBaseJson();
            JSONObject data = new JSONObject();
            vertifCode.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", username);
            result = vertifCode.toString();
        } catch (Exception e)
        {
        }
        return result;
    }

    /**
     * 账户余额支付
     */
    public static final String getPayment(String username, String id,
            int payment_mode, int currency_unit, String total_fee)
    {

        String result = "";
        try
        {
            JSONObject payment = generateBaseJson();
            JSONObject data = new JSONObject();
            payment.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", username);

            JSONObject oder = new JSONObject();
            data.put("order", oder);
            oder.put("id", id);
            oder.put("payment_mode", Integer.toString(payment_mode));
            oder.put("currency_unit", Integer.toString(currency_unit));
            oder.put("total_fee", total_fee);

            result = payment.toString();

        } catch (Exception e)
        {
        }
        return result;
    }

    /**
     * 重置密码
     */
    public static final String resetChallenge(String username,
            String id_card_number, Activity activity)
    {
        String result = "";
        try
        {
            JSONObject verifychallenge = generateBaseJson();
            JSONObject data = new JSONObject();
            verifychallenge.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", username);
            data.put("verification_code", id_card_number);
            result = verifychallenge.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public static final String getMessage(String username, int link_source,
            int max_id)
    {
        String result = "";
        try
        {
            JSONObject loginRequest = generateBaseJson();
            JSONObject data = new JSONObject();
            loginRequest.put("data", data);
            data.put("link_source", Integer.toString(link_source));
            data.put("block", "0");
            data.put("max_id", Integer.toString(max_id));
            data.put("username", username);

            result = loginRequest.toString();
        } catch (Exception e)
        {
        }

        return result;
    }

    public static final String notifyMe(String username, int waittime,
            int link_source)
    {
        String result = "";
        try
        {
            JSONObject loginRequest = generateBaseJson();
            JSONObject data = new JSONObject();
            loginRequest.put("data", data);
            data.put("link_source", Integer.toString(link_source));
            data.put("username", username);
            data.put("waittime", Integer.toString(waittime));

            result = loginRequest.toString();
        } catch (Exception e)
        {
        }

        return result;
    }

    public static TraMessage generateSendMessage(String username, String message,
            String to, int type, String ext_type, int from_type, int to_type,
            int msg_type, String nick_name, String group_name,
            String head_portrait, String fromHeadPath)
    {
        TraMessage message2 = new TraMessage();
        message2.setFrom(username);
        message2.setFrom_type(from_type);
        message2.setTo(to);
        message2.setTo_type(to_type);
        message2.setTime(TimeUtil.getDateString(System.currentTimeMillis(),
                TimeUtil.dateFormat2));
        message2.setPriority(2);
        message2.setType(type);
        message2.setContent(message);
        message2.setUser_name(username);
        message2.setWidth_user(to);
        message2.setExt_type(ext_type);
        message2.setMsg_type(msg_type);
        message2.setNick_name(nick_name);
        message2.setGroup_name(group_name);
        message2.setHead_portrait(head_portrait);
        message2.setFrom_head_portrait(fromHeadPath);
        message2.setHead_portrait_url(FileUtil.getImagePath("head_img") + "/"
                + fromHeadPath + "_s" + ".jpg");
//        message2.setContact_id(TravelService.setContactId(message2));
        message2.setContact_id(FetchMessage.setContactId(message2));

        return message2;
    }

    /**
     * 发送
     */
    public static String sendMessage(String username, TraMessage message2,
            int linkSource)
    {
        String result = "";
        try
        {
            JSONObject vertifCode = generateBaseJson();
            JSONObject data = new JSONObject();
            vertifCode.put("data", data);

            data.put("link_source", Integer.toString(linkSource));
            data.put("username", username);

            JSONArray message_list = new JSONArray();

            JSONObject messageJson = message2.generateJson();
            message_list.put(messageJson);
            data.put("message_list", message_list);

            result = vertifCode.toString();
        } catch (Exception e)
        {
        }

        return result;
    }

    /**
     * 群成员信息
     */
    public static final String getGroupMsg(int version, String id)
    {
        String result = "";
        try
        {
            JSONObject json = generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            data.put("version", Integer.toString(version));
            data.put("id", id);

            result = json.toString();

        } catch (Exception e)
        {

            e.printStackTrace();
        }

        return result;
    }

    /**
     * new Group
     */

    public static final String newGroup(String groupName, List<TagNumber> list,
            int type, String expireddate)
    {
        String result = "";
        try
        {
            JSONObject json = generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            data.put("name", groupName);
            data.put("expireddate", expireddate);
            JSONArray jsonArray = new JSONArray();
            data.put("member_list", jsonArray);

            if (list != null)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    JSONObject groupInfoJsonObject = new JSONObject();
                    groupInfoJsonObject.put("number", list.get(i).getNewNum());
                    groupInfoJsonObject.put("type", Integer.toString(type));
                    jsonArray.put(groupInfoJsonObject);
                }
            }

            result = json.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    // 获取呼转号码
    public static final String getCallforwardNumber(int crbt_id)
    {
        String result = "";
        try
        {
            JSONObject json = generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);

            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            data.put("sim_mcc", Engine.getInstance().getSimMcc());
            data.put("sim_mnc", Engine.getInstance().getSimMnc());
            data.put("crbt_id", Integer.toString(crbt_id));

            result = json.toString();
        } catch (Exception e)
        {
        }
        return result;
    }

    /**
     * 群组添加新成员
     */

    public static final String addMember(String groupId, List<TagNumber> list)
    {
        String result = "";
        try
        {
            JSONObject json = generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            data.put("id", groupId);
            JSONArray jsonArray = new JSONArray();
            data.put("member_list", jsonArray);

            if (list != null)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    JSONObject groupInfoJsonObject = new JSONObject();
                    groupInfoJsonObject.put("number", list.get(i).getNewNum());
                    jsonArray.put(groupInfoJsonObject);
                }
            }
            result = json.toString();
        } catch (Exception e)
        {

            e.printStackTrace();
        }

        return result;
    }

    /**
     * 群组删除成员
     */

    public static final String deleteMember(String groupId, List<GroupMsg> list)
    {
        String result = "";
        try
        {
            JSONObject json = generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            data.put("id", groupId);
            JSONArray jsonArray = new JSONArray();
            data.put("member_list", jsonArray);

            if (list != null)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    JSONObject groupInfoJsonObject = new JSONObject();
                    groupInfoJsonObject.put("number", list.get(i).getNumber());
                    jsonArray.put(groupInfoJsonObject);
                }
            }
            result = json.toString();
        } catch (Exception e)
        {

            e.printStackTrace();
        }
        return result;
    }

    /**
     * 更新群名称
     */
    public static final String updateGroupInfo(String groupId,
            String nick_group_name, String expireddate)
    {
        String result = "";
        try
        {
            JSONObject json = generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            data.put("id", groupId);
            data.put("name", nick_group_name);
            data.put("expireddate", expireddate);
            result = json.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除并退出群
     */
    public static final String quitGroup(String groupId)
    {
        String result = "";
        try
        {
            JSONObject json = generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            data.put("id", groupId);
            result = json.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获得行程信息
     */
    public static final String getTripInfo(String username,
            List<TripInfoList> list)
    {
        String result = "";
        try
        {
            JSONObject json = generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", username);
            data.put("tripidlist", list);
            JSONArray jArray = new JSONArray();
            data.put("tripidlist", jArray);

            if (list != null)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    jArray.put(list.get(i).getTripid());
                }
            }

            result = json.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获得所有群列表
     */
    public static final String getGroupList() {

        String result = "";
        try
        {
            JSONObject json = generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            result = json.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 设置NoRoaming状态
     */
    public static final String setNoamingState(String starttime, String endtime, String timezone,
            String type) {

        String result = "";
        try
        {
            JSONObject json = generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            data.put("starttime", starttime);
            data.put("endtime", endtime);
            data.put("timezone", timezone);
            data.put("type", type);

            result = json.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获得NoRoaming状态
     */
    public static final String getNoamingState() {

        String result = "";
        try
        {
            JSONObject json = generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            result = json.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
