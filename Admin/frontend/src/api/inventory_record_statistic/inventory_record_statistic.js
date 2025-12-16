// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/inventoryRecordStatistics";

export default {
inventory_record_expire_date_datetime_statistic_a38e9712_count(data){
  return service({
    url: BASE_API + "/inventory_record_expire_date_datetime_statistic_a38e9712_count",
    method: "post",
    data:data
  });
},
inventory_record_expire_date_datetime_statistic_00541631_count(data){
  return service({
    url: BASE_API + "/inventory_record_expire_date_datetime_statistic_00541631_count",
    method: "post",
    data:data
  });
},
inventory_record_expire_date_datetime_statistic_5296a1bd_count(data){
  return service({
    url: BASE_API + "/inventory_record_expire_date_datetime_statistic_5296a1bd_count",
    method: "post",
    data:data
  });
},
};
