select
  municipality_id,
  AVG( actual_price / base_price ) average
from
  kako_data
group by
  municipality_id