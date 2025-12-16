// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/liveStreamSyncStatistics";

export default {
live_stream_sync_sync_time_datetime_statistic_f04701f0_count(data){
  return service({
    url: BASE_API + "/live_stream_sync_sync_time_datetime_statistic_f04701f0_count",
    method: "post",
    data:data
  });
},
live_stream_sync_sync_time_datetime_statistic_9943545f_count(data){
  return service({
    url: BASE_API + "/live_stream_sync_sync_time_datetime_statistic_9943545f_count",
    method: "post",
    data:data
  });
},
live_stream_sync_sync_time_datetime_statistic_5ec92ac3_count(data){
  return service({
    url: BASE_API + "/live_stream_sync_sync_time_datetime_statistic_5ec92ac3_count",
    method: "post",
    data:data
  });
},
};
