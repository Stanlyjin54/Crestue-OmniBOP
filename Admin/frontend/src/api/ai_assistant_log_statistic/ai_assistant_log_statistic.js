// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/aiAssistantLogStatistics";

export default {
ai_assistant_log_query_time_datetime_statistic_9b682c72_count(data){
  return service({
    url: BASE_API + "/ai_assistant_log_query_time_datetime_statistic_9b682c72_count",
    method: "post",
    data:data
  });
},
ai_assistant_log_query_time_datetime_statistic_df5261a6_count(data){
  return service({
    url: BASE_API + "/ai_assistant_log_query_time_datetime_statistic_df5261a6_count",
    method: "post",
    data:data
  });
},
ai_assistant_log_query_time_datetime_statistic_d691e2ab_count(data){
  return service({
    url: BASE_API + "/ai_assistant_log_query_time_datetime_statistic_d691e2ab_count",
    method: "post",
    data:data
  });
},
};
