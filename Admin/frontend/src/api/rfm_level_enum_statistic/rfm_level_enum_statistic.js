// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/rfmLevelEnumStatistics";

export default {
rfm_level_enum_rfm_level_enum_id_status_statistic_37bad8f0_count(data){
  return service({
    url: BASE_API + "/rfm_level_enum_rfm_level_enum_id_status_statistic_37bad8f0_count",
    method: "post",
    data:data
  });
},
rfm_level_enum_rfm_level_enum_id_status_statistic_23d416cb_count(data){
  return service({
    url: BASE_API + "/rfm_level_enum_rfm_level_enum_id_status_statistic_23d416cb_count",
    method: "post",
    data:data
  });
},
rfm_level_enum_rfm_level_enum_id_status_statistic_bcb4c0b6_count(data){
  return service({
    url: BASE_API + "/rfm_level_enum_rfm_level_enum_id_status_statistic_bcb4c0b6_count",
    method: "post",
    data:data
  });
},
};
