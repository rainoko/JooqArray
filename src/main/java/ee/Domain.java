package ee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Domain {

  private Long id;
  private List<Integer> dataarr = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<Integer> getDataarr() {
    return dataarr;
  }

  public void setDataarr(Integer[] in) {
//    dataArr = in;
    Arrays.stream(in).forEach(i -> dataarr.add((Integer)i));
  }
}
