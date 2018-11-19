package curtain.photograph.com.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import base.injectionview.Id;
import base.injectionview.Layout;
import curtain.photograph.com.R;
import curtain.photograph.com.base.AppBaseActivity;
import curtain.photograph.com.common.model.CurtainMaterail;
import curtain.photograph.com.ui.adapter.CurtaiMaterialAdapter;
import curtain.photograph.com.ui.customview.HorizontalRefreshLayout;

@Layout(R.layout.activity_test)
public class TestActivity extends AppBaseActivity {
    @Id(R.id.refreshParent)
    HorizontalRefreshLayout refreshParent;
    @Id(R.id.dataRecyclear)
    RecyclerView dataRecyclear;
    private CurtaiMaterialAdapter mCurtaiMaterail;
    public List<CurtainMaterail> mItems = new ArrayList<CurtainMaterail>(
            Arrays.asList(
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail()));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        LinearLayoutManager linearLayoutManagerHead=new LinearLayoutManager(mContext);
        linearLayoutManagerHead.setOrientation(LinearLayoutManager.HORIZONTAL);
        dataRecyclear.setLayoutManager(linearLayoutManagerHead);
        mCurtaiMaterail= new CurtaiMaterialAdapter(mContext);
        dataRecyclear.setAdapter(mCurtaiMaterail);
        mCurtaiMaterail.setDatas(mItems);
    }


}
