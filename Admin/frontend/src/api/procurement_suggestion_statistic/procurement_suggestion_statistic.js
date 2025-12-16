// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/procurementSuggestionStatistics";

export default {
procurement_suggestion_suggest_date_datetime_statistic_ec8b49be_count(data){
  return service({
    url: BASE_API + "/procurement_suggestion_suggest_date_datetime_statistic_ec8b49be_count",
    method: "post",
    data:data
  });
},
procurement_suggestion_suggest_date_datetime_statistic_26e934d3_count(data){
  return service({
    url: BASE_API + "/procurement_suggestion_suggest_date_datetime_statistic_26e934d3_count",
    method: "post",
    data:data
  });
},
procurement_suggestion_suggest_date_datetime_statistic_00313900_count(data){
  return service({
    url: BASE_API + "/procurement_suggestion_suggest_date_datetime_statistic_00313900_count",
    method: "post",
    data:data
  });
},
};
