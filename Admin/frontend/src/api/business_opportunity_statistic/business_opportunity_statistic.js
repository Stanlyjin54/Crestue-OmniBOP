// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/businessOpportunityStatistics";

export default {
business_opportunity_status_status_statistic_7faf0bdd_count(data){
  return service({
    url: BASE_API + "/business_opportunity_status_status_statistic_7faf0bdd_count",
    method: "post",
    data:data
  });
},
business_opportunity_status_status_statistic_5ba2a19d_count(data){
  return service({
    url: BASE_API + "/business_opportunity_status_status_statistic_5ba2a19d_count",
    method: "post",
    data:data
  });
},
business_opportunity_status_status_statistic_3d13a485_count(data){
  return service({
    url: BASE_API + "/business_opportunity_status_status_statistic_3d13a485_count",
    method: "post",
    data:data
  });
},
};
