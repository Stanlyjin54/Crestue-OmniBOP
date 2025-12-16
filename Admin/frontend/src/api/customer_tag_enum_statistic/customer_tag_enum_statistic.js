// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/customerTagEnumStatistics";

export default {
customer_tag_enum_customer_tag_enum_id_status_statistic_9083f377_count(data){
  return service({
    url: BASE_API + "/customer_tag_enum_customer_tag_enum_id_status_statistic_9083f377_count",
    method: "post",
    data:data
  });
},
customer_tag_enum_customer_tag_enum_id_status_statistic_027cbb5f_count(data){
  return service({
    url: BASE_API + "/customer_tag_enum_customer_tag_enum_id_status_statistic_027cbb5f_count",
    method: "post",
    data:data
  });
},
customer_tag_enum_customer_tag_enum_id_status_statistic_73a04ddf_count(data){
  return service({
    url: BASE_API + "/customer_tag_enum_customer_tag_enum_id_status_statistic_73a04ddf_count",
    method: "post",
    data:data
  });
},
};
