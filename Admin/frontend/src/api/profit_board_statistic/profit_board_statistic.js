// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/profitBoardStatistics";

export default {
profit_board_stat_date_datetime_statistic_17470edd_count(data){
  return service({
    url: BASE_API + "/profit_board_stat_date_datetime_statistic_17470edd_count",
    method: "post",
    data:data
  });
},
profit_board_stat_date_datetime_statistic_edf98856_count(data){
  return service({
    url: BASE_API + "/profit_board_stat_date_datetime_statistic_edf98856_count",
    method: "post",
    data:data
  });
},
profit_board_stat_date_datetime_statistic_f80cc477_count(data){
  return service({
    url: BASE_API + "/profit_board_stat_date_datetime_statistic_f80cc477_count",
    method: "post",
    data:data
  });
},
};
