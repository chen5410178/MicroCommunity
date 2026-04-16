<template>
  <div>
    <el-dialog title="切换小区" :visible.sync="dialogVisible" width="70%" :close-on-click-modal="false">
      <div class="filter-container text-right">
        <el-input :placeholder="$t('communityManage.table.name')" v-model="navCommunityInfo.searchCommunityName"
          style="width: 300px;" class="filter-item"></el-input>
        <el-button class="filter-item" type="primary" icon="el-icon-search" @click="_queryEnterCommunity">
          {{ $t('common.search') }}
        </el-button>
      </div>

      <el-table :data="communitys" border fit highlight-current-row style="width: 100%; margin-top: 20px;">
        <el-table-column :label="$t('communityManage.table.communityId')" prop="communityId"
          align="center"></el-table-column>
        <el-table-column :label="$t('communityManage.table.name')" prop="name" align="center"></el-table-column>
        <el-table-column :label="$t('common.operation')" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="_chooseCurrentCommunity(scope.row)">
              {{ $t('common.select') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
          layout="total, prev, pager, next" :total="total"></el-pagination>
      </div>
    </el-dialog>
  </div>
</template>
  
<script>
import { getMyEnteredCommunitys } from '@/api/community/communityApi'
import { setCurrentCommunity } from "@/utils/vc"

const DEFAULT_PAGE = 1;
const DEFAULT_ROW = 10;


export default {
  name: 'ChooseEnterCommunity',
  data() {
    return {
      dialogVisible: false,
      currentPage: DEFAULT_PAGE,
      pageSize: DEFAULT_ROW,
      total: 0,
      communitys: [],
      navCommunityInfo: {
        _currentCommunity: {},
        communityInfos: [],
        communityInfo: [],
        errorInfo: '',
        searchCommunityName: ''
      }
    }
  },
  created() {
  },
  methods: {
    open() {
      this.dialogVisible = true;
      this.navCommunityInfo.searchCommunityName = '';
      this.listEnterCommunity(DEFAULT_PAGE, DEFAULT_ROW);
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.listEnterCommunity(val, DEFAULT_ROW);
    },
    async listEnterCommunity(_page, _row) {

      const { communitys, total } = await getMyEnteredCommunitys({
        _uid: '123mlkdinkldldijdhuudjdjkkd',
        page: _page,
        row: _row,
        communityName: this.navCommunityInfo.searchCommunityName
      })
      this.communitys = communitys
      this.total = total;
      this.currentPage = _page;

    },
    _chooseCurrentCommunity(_currentCommunity) {
      setCurrentCommunity(_currentCommunity)
      
      this.dialogVisible = false;
      window.location.href="/"
    },
    _queryEnterCommunity() {
      this.listEnterCommunity(DEFAULT_PAGE, DEFAULT_ROW);
    }
  }
}
</script>
  
<style scoped>
.filter-container {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: center;
}
</style>