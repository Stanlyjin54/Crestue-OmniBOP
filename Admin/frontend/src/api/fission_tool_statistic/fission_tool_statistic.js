// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/fissionToolStatistics";

export default {
fission_tool_start_date_datetime_statistic_79c789ea_count(data){
  return service({
    url: BASE_API + "/fission_tool_start_date_datetime_statistic_79c789ea_count",
    method: "post",
    data:data
  });
},
fission_tool_start_date_datetime_statistic_a2b0c036_count(data){
  return service({
    url: BASE_API + "/fission_tool_start_date_datetime_statistic_a2b0c036_count",
    method: "post",
    data:data
  });
},
fission_tool_start_date_datetime_statistic_a11aa4e6_count(data){
  return service({
    url: BASE_API + "/fission_tool_start_date_datetime_statistic_a11aa4e6_count",
    method: "post",
    data:data
  });
},
fission_tool_end_date_datetime_statistic_308b2d65_count(data){
  return service({
    url: BASE_API + "/fission_tool_end_date_datetime_statistic_308b2d65_count",
    method: "post",
    data:data
  });
},
fission_tool_end_date_datetime_statistic_955257d7_count(data){
  return service({
    url: BASE_API + "/fission_tool_end_date_datetime_statistic_955257d7_count",
    method: "post",
    data:data
  });
},
fission_tool_end_date_datetime_statistic_700394af_count(data){
  return service({
    url: BASE_API + "/fission_tool_end_date_datetime_statistic_700394af_count",
    method: "post",
    data:data
  });
},
};
