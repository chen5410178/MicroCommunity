<template>
  <div class="animated fadeInRight padding">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('myResourceStoreManage.queryCondition') }}</span>
            <div style="float: right;">
              <el-button type="text" @click="_moreCondition">
                {{ myResourceStoreManageInfo.moreCondition ? $t('myResourceStoreManage.hide') : $t('myResourceStoreManage.more') }}
              </el-button>
            </div>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input
                v-model="myResourceStoreManageInfo.conditions.resId"
                :placeholder="$t('myResourceStoreManage.enterResId')"
                clearable
              />
            </el-col>
            <el-col :span="8">
              <el-input
                v-model="myResourceStoreManageInfo.conditions.resName"
                :placeholder="$t('myResourceStoreManage.enterResName')"
                clearable
              />
            </el-col>
            <el-col :span="6">
              <el-input
                v-model="myResourceStoreManageInfo.conditions.resCode"
                :placeholder="$t('myResourceStoreManage.enterResCode')"
                clearable
              />
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="_queryResourceStoreMethod">
                <i class="el-icon-search"></i>
                {{ $t('myResourceStoreManage.search') }}
              </el-button>
              <el-button @click="_resetResourceStoreMethod">
                <i class="el-icon-refresh"></i>
                {{ $t('myResourceStoreManage.reset') }}
              </el-button>
            </el-col>
          </el-row>

          <el-row v-show="myResourceStoreManageInfo.moreCondition" :gutter="20" style="margin-top: 20px;">
            <el-col :span="6">
              <el-select
                v-model="myResourceStoreManageInfo.conditions.parentRstId"
                :placeholder="$t('myResourceStoreManage.selectResType')"
                style="width: 100%;"
                @change="_listResourceStoreSonTypes"
              >
                <el-option
                  v-for="(item,index) in myResourceStoreManageInfo.resourceStoreTypes"
                  :key="index"
                  :label="item.name"
                  :value="item.rstId"
                />
              </el-select>
            </el-col>
            <el-col :span="8">
              <el-select
                v-model="myResourceStoreManageInfo.conditions.rstId"
                :placeholder="$t('myResourceStoreManage.selectSecondType')"
                style="width: 100%;"
                @change="_listResourceStoreSpecifications"
              >
                <el-option
                  v-for="(item,index) in myResourceStoreManageInfo.resourceStoreSonTypes"
                  :key="index"
                  :label="item.name"
                  :value="item.rstId"
                />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-select
                v-model="myResourceStoreManageInfo.conditions.rssId"
                :placeholder="$t('myResourceStoreManage.selectResSpec')"
                style="width: 100%;"
              >
                <el-option
                  v-for="(item,index) in myResourceStoreManageInfo.resourceStoreSpecifications"
                  :key="index"
                  :label="item.specName"
                  :value="item.rssId"
                />
              </el-select>
            </el-col>
          </el-row>

          <el-row v-show="myResourceStoreManageInfo.moreCondition" :gutter="20" style="margin-top: 20px;">
            <el-col v-if="hasPrivilege('502021071018550002')" :span="6">
              <el-input
                v-model="myResourceStoreManageInfo.conditions.searchUserId"
                :placeholder="$t('myResourceStoreManage.enterUserId')"
                clearable
              />
            </el-col>
            <el-col v-if="hasPrivilege('502021071018550002')" :span="8">
              <el-input
                v-model="myResourceStoreManageInfo.conditions.searchUserName"
                :placeholder="$t('myResourceStoreManage.enterUserName')"
                clearable
              />
            </el-col>
            <el-col :span="6">
              <el-select
                v-model="myResourceStoreManageInfo.conditions.isFixed"
                :placeholder="$t('myResourceStoreManage.selectIsFixed')"
                style="width: 100%;"
              >
                <el-option
                  v-for="(item,index) in myResourceStoreManageInfo.isFixeds"
                  :key="index"
                  :label="item.name"
                  :value="item.statusCd"
                />
              </el-select>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('myResourceStoreManage.myResources') }}</span>
            <div style="float: right;">
              <el-button type="primary" size="small" @click="_jump2ReturnGoodsPage">
                <i class="el-icon-circle-close"></i>
                {{ $t('myResourceStoreManage.return') }}
              </el-button>
              <el-button type="primary" size="small" @click="_jump2TransferGoodsPage">
                <i class="el-icon-share"></i>
                {{ $t('myResourceStoreManage.transfer') }}
              </el-button>
              <el-button type="primary" size="small" @click="_jump2ScrapGoodsPage">
                <i class="el-icon-delete"></i>
                {{ $t('myResourceStoreManage.scrap') }}
              </el-button>
              <el-button type="primary" size="small" @click="_exportMyResourceStore">
                <i class="el-icon-download"></i>
                {{ $t('myResourceStoreManage.export') }}
              </el-button>
            </div>
          </div>
          <el-table
            :data="myResourceStoreManageInfo.resourceStores"
            border
            style="width: 100%"
          >
            <el-table-column
              v-if="hasPrivilege('502021071018550002')"
              prop="userId"
              :label="$t('myResourceStoreManage.userId')"
              align="center"
            />
            <el-table-column
              v-if="hasPrivilege('502021071018550002')"
              prop="userName"
              :label="$t('myResourceStoreManage.userName')"
              align="center"
            />
            <el-table-column
              prop="resId"
              :label="$t('myResourceStoreManage.resId')"
              align="center"
            />
            <el-table-column
              :label="$t('myResourceStoreManage.resType')"
              align="center"
            >
              <template slot-scope="scope">
                {{ scope.row.parentRstName }} > {{ scope.row.rstName }}
              </template>
            </el-table-column>
            <el-table-column
              prop="resName"
              :label="$t('myResourceStoreManage.resName')"
              align="center"
            />
            <el-table-column
              prop="specName"
              :label="$t('myResourceStoreManage.resSpec')"
              align="center"
            >
              <template slot-scope="scope">
                {{ scope.row.specName || '-' }}
              </template>
            </el-table-column>
            <el-table-column
              prop="resCode"
              :label="$t('myResourceStoreManage.resCode')"
              align="center"
            />
            <el-table-column
              prop="isFixedName"
              :label="$t('myResourceStoreManage.isFixed')"
              align="center"
            />
            <el-table-column
              :label="$t('myResourceStoreManage.stock')"
              align="center"
            >
              <template slot-scope="scope">
                {{ scope.row.stock }}{{ scope.row.unitCodeName }}
              </template>
            </el-table-column>
            <el-table-column
              :label="$t('myResourceStoreManage.miniStock')"
              align="center"
            >
              <template slot-scope="scope">
                {{ scope.row.miniStock }}{{ scope.row.miniUnitCodeName }}
              </template>
            </el-table-column>
            <el-table-column
              :label="$t('myResourceStoreManage.miniUnit')"
              align="center"
            >
              <template slot-scope="scope">
                1{{ scope.row.unitCodeName }}={{ scope.row.miniUnitStock }}{{ scope.row.miniUnitCodeName }}
              </template>
            </el-table-column>
            <el-table-column
              :label="$t('myResourceStoreManage.price')"
              align="center"
            >
              <template slot-scope="scope">
                {{ scope.row.outHighPrice === scope.row.outLowPrice ? '￥' + scope.row.outLowPrice : '￥' + scope.row.outLowPrice + '-￥' + scope.row.outHighPrice }}
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            :current-page.sync="page.current"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="page.size"
            :total="page.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listUserStorehouses, listResourceStoreTypes, listResourceStoreSpecifications,exportData } from '@/api/resource/myResourceStoreManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getUserId,getUserName } from '@/api/user/userApi'


export default {
  name: 'MyResourceStoreManageList',
  data() {
    return {
      myResourceStoreManageInfo: {
        resourceStores: [],
        total: 0,
        records: 1,
        moreCondition: false,
        isFixeds: [],
        conditions: {
          resId: '',
          resName: '',
          resCode: '',
          parentRstId: '',
          rstId: '',
          rssId: '',
          searchUserName: '',
          searchUserId: '',
          isFixed: '',
          userId: getUserId(),
          userName: getUserName(),
          communityId: ''
        },
        resourceStoreTypes: [],
        resourceStoreSonTypes: [],
        resourceStoreSpecifications: []
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.myResourceStoreManageInfo.conditions.communityId = getCommunityId()
    this._initMethod()
  },
  methods: {
    async _initMethod() {
      await this._listResourceStores(this.page.current, this.page.size)
      await this._listResourceStoreTypes()
      await this._listResourceStoreSpecifications()
      await this.getDictData()
    },
    async getDictData() {
      try {
        const data = await getDict('resource_store', 'is_fixed')
        this.myResourceStoreManageInfo.isFixeds = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _listResourceStores(page, size) {
      try {
        const params = {
          ...this.myResourceStoreManageInfo.conditions,
          page,
          row: size
        }
        const res = await listUserStorehouses(params)
        this.myResourceStoreManageInfo.resourceStores = res.data
        this.page.total = res.total
      } catch (error) {
        console.error('获取物品列表失败:', error)
      }
    },
    _queryResourceStoreMethod() {
      this.page.current = 1
      this._listResourceStores(this.page.current, this.page.size)
    },
    _resetResourceStoreMethod() {
      this.myResourceStoreManageInfo.conditions = {
        ...this.myResourceStoreManageInfo.conditions,
        resId: '',
        resName: '',
        resCode: '',
        rstId: '',
        parentRstId: '',
        rssId: '',
        searchUserId: '',
        searchUserName: '',
        isFixed: ''
      }
      this.myResourceStoreManageInfo.resourceStoreSonTypes = []
      this.myResourceStoreManageInfo.resourceStoreSpecifications = []
      this._listResourceStores(this.page.current, this.page.size)
    },
    _moreCondition() {
      this.myResourceStoreManageInfo.moreCondition = !this.myResourceStoreManageInfo.moreCondition
    },
    async _listResourceStoreTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.myResourceStoreManageInfo.conditions.communityId,
          parentId: '0'
        }
        const res = await listResourceStoreTypes(params)
        this.myResourceStoreManageInfo.resourceStoreTypes = res.data
      } catch (error) {
        console.error('获取物品类型失败:', error)
      }
    },
    async _listResourceStoreSonTypes() {
      this.myResourceStoreManageInfo.conditions.rstId = ''
      this.myResourceStoreManageInfo.resourceStoreSonTypes = []
      if (!this.myResourceStoreManageInfo.conditions.parentRstId) return

      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.myResourceStoreManageInfo.conditions.communityId,
          parentId: this.myResourceStoreManageInfo.conditions.parentRstId
        }
        const res = await listResourceStoreTypes(params)
        this.myResourceStoreManageInfo.resourceStoreSonTypes = res.data
      } catch (error) {
        console.error('获取二级分类失败:', error)
      }
    },
    async _listResourceStoreSpecifications() {
      this.myResourceStoreManageInfo.resourceStoreSpecifications = []
      this.myResourceStoreManageInfo.conditions.rssId = ''
      if (!this.myResourceStoreManageInfo.conditions.rstId) return

      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.myResourceStoreManageInfo.conditions.communityId,
          rstId: this.myResourceStoreManageInfo.conditions.rstId
        }
        const res = await listResourceStoreSpecifications(params)
        this.myResourceStoreManageInfo.resourceStoreSpecifications = res.data
      } catch (error) {
        console.error('获取物品规格失败:', error)
      }
    },
    _jump2TransferGoodsPage() {
      this.$router.push('/pages/common/transferGoodsManage')
    },
    _jump2ScrapGoodsPage() {
      this.$router.push('/pages/common/scrapGoodsStep')
    },
    _jump2ReturnGoodsPage() {
      this.$router.push('/pages/common/returnStorehouseApplyManage')
    },
    async _exportMyResourceStore() {
      try {
        const params = {
          ...this.myResourceStoreManageInfo.conditions,
          pagePath: 'myResourceStoreManage'
        }
        const res = await exportData(params)
        this.$message.success(res.msg)
        if (res.code === 0) {
          this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
        }
      } catch (error) {
        console.error('导出失败:', error)
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listResourceStores(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listResourceStores(this.page.current, this.page.size)
    },
  }
}
</script>

<style scoped>
.animated {
  animation-duration: 0.5s;
}
</style>