// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/userInfoStatistics";

export default {
user_info_registration_date_datetime_statistic_2ec47ef3_count(data){
  return service({
    url: BASE_API + "/user_info_registration_date_datetime_statistic_2ec47ef3_count",
    method: "post",
    data:data
  });
},
user_info_registration_date_datetime_statistic_2de39a8d_count(data){
  return service({
    url: BASE_API + "/user_info_registration_date_datetime_statistic_2de39a8d_count",
    method: "post",
    data:data
  });
},
user_info_registration_date_datetime_statistic_2d54c52e_count(data){
  return service({
    url: BASE_API + "/user_info_registration_date_datetime_statistic_2d54c52e_count",
    method: "post",
    data:data
  });
},
};
