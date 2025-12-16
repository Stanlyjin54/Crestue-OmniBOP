// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/permissionLogStatistics";

export default {
permission_log_action_time_datetime_statistic_84170e10_count(data){
  return service({
    url: BASE_API + "/permission_log_action_time_datetime_statistic_84170e10_count",
    method: "post",
    data:data
  });
},
permission_log_action_time_datetime_statistic_975db700_count(data){
  return service({
    url: BASE_API + "/permission_log_action_time_datetime_statistic_975db700_count",
    method: "post",
    data:data
  });
},
permission_log_action_time_datetime_statistic_8cf06955_count(data){
  return service({
    url: BASE_API + "/permission_log_action_time_datetime_statistic_8cf06955_count",
    method: "post",
    data:data
  });
},
};
