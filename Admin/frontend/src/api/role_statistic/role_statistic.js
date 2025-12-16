// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/roleStatistics";

export default {
role_create_time_datetime_statistic_f1dec0e3_count(data){
  return service({
    url: BASE_API + "/role_create_time_datetime_statistic_f1dec0e3_count",
    method: "post",
    data:data
  });
},
role_create_time_datetime_statistic_30529d9f_count(data){
  return service({
    url: BASE_API + "/role_create_time_datetime_statistic_30529d9f_count",
    method: "post",
    data:data
  });
},
role_create_time_datetime_statistic_99f55dc9_count(data){
  return service({
    url: BASE_API + "/role_create_time_datetime_statistic_99f55dc9_count",
    method: "post",
    data:data
  });
},
};
