<template>
  <div class="inspection-point-container">
    <el-row class="margin-bottom-20" :gutter="20">
      <el-col :span="4">
        <el-card>
          <div class="flex justify-start">
            <div style="width: 300px; margin-right: 10px;">
              <el-input :placeholder="$t('inspectionPoint.placeholder.inspectionName')"
                v-model="inspectionPointInfo.conditions.inspectionName" clearable />
            </div>
            <el-button type="text" class="margin-left-xs" size="small" @click="_queryInspectionPointMethod()">
              {{ $t('common.search') }}
            </el-button>
            <el-button type="text" class="margin-left-xs" size="small" @click="_openAddInspectionPointModal()">
              {{ $t('common.add') }}
            </el-button>
          </div>

          <div class="vc-org margin-top">
            <ul class="point-list">
              <li v-for="(point, index) in inspectionPointInfo.points" :key="index"
                @click="_switchInspectionPoint(point)"
                :class="{ 'active': point.inspectionId == inspectionPointInfo.point.inspectionId }">
                {{ point.inspectionName }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-row v-if="inspectionPointInfo.point.inspectionId">
          <el-col :span="24">
            <el-card>
              <div slot="header" class="flex justify-between">
                <span>{{ $t('inspectionPoint.title') }}</span>
                <div class="ibox-tools">
                  <el-button size="small" class="margin-left-xs"
                    @click="_openEditInspectionPointModel(inspectionPointInfo.point)">
                    {{ $t('common.edit') }}
                  </el-button>
                  <el-button size="small" type="danger" class="margin-left-xs"
                    @click="_openDeleteInspectionPointModel(inspectionPointInfo.point)">
                    {{ $t('common.delete') }}
                  </el-button>
                  <el-button size="small" class="margin-left-xs" @click="_pointQrCode(inspectionPointInfo.point)">
                    {{ $t('inspectionPoint.qrCode') }}
                  </el-button>
                </div>
              </div>

              <div class="">
                <el-row :gutter="20" class="text-left">
                  <el-col :span="6">
                    <div class="form-group">
                      <label class="col-form-label">
                        {{ $t('inspectionPoint.inspectionPoint') }}:
                      </label>
                      <label>{{ inspectionPointInfo.point.inspectionName }}</label>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="form-group">
                      <label class="col-form-label">
                        {{ $t('inspectionPoint.inspectionType') }}:
                      </label>
                      <label>{{ inspectionPointInfo.point.pointTypeName }}</label>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="form-group">
                      <label class="col-form-label">
                        {{ $t('inspectionPoint.inspectionLocation') }}:
                      </label>
                      <label>{{ inspectionPointInfo.point.pointObjName }}</label>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="form-group">
                      <label class="col-form-label">
                        {{ $t('inspectionPoint.inspectionItem') }}:
                      </label>
                      <label>{{ inspectionPointInfo.point.itemName }}</label>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="form-group">
                      <label class="col-form-label">
                        NFC:
                      </label>
                      <label>{{ inspectionPointInfo.point.nfcCode }}</label>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row v-if="inspectionPointInfo.point.inspectionId" class="margin-top">
          <el-col :span="24">
            <el-card>
              <el-tabs v-model="inspectionPointInfo._currentTab"
                @tab-click="changeTab(inspectionPointInfo._currentTab)">
                <el-tab-pane name="pointTaskDetail" :label="$t('inspectionPoint.tabs.pointTaskDetail')">
                  <point-task-detail v-if="inspectionPointInfo._currentTab === 'pointTaskDetail'"
                    ref="pointTaskDetail" />
                </el-tab-pane>
                <el-tab-pane name="pointRoute" :label="$t('inspectionPoint.tabs.pointRoute')">
                  <point-route v-if="inspectionPointInfo._currentTab === 'pointRoute'" ref="pointRoute" />
                </el-tab-pane>
                <el-tab-pane name="pointPlan" :label="$t('inspectionPoint.tabs.pointPlan')">
                  <point-plan v-if="inspectionPointInfo._currentTab === 'pointPlan'" ref="pointPlan" />
                </el-tab-pane>
                <el-tab-pane name="pointTask" :label="$t('inspectionPoint.tabs.pointTask')">
                  <point-task v-if="inspectionPointInfo._currentTab === 'pointTask'" ref="pointTask" />
                </el-tab-pane>
              </el-tabs>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <!-- 模态框组件 -->
    <add-inspection-point ref="addInspectionPoint" @success="_listInspectionPoints" />
    <edit-inspection-point ref="editInspectionPoint" @success="_listInspectionPoints" />
    <delete-inspection-point ref="deleteInspectionPoint" @success="_listInspectionPoints" />
    <inspection-point-qr-code ref="inspectionPointQrCode" @success="_listInspectionPoints" />
  </div>
</template>

<script>
import { listInspectionPoints } from '@/api/inspection/inspectionPointApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import PointTaskDetail from '@/components/inspection/pointTaskDetail'
import PointRoute from '@/components/inspection/pointRoute'
import PointPlan from '@/components/inspection/pointPlan'
import PointTask from '@/components/inspection/pointTask'
import AddInspectionPoint from '@/components/inspection/addInspectionPoint'
import EditInspectionPoint from '@/components/inspection/editInspectionPoint'
import DeleteInspectionPoint from '@/components/inspection/deleteInspectionPoint'
import InspectionPointQrCode from '@/components/inspection/inspectionPointQrCode'

export default {
  name: 'InspectionPointList',
  components: {
    PointTaskDetail,
    PointRoute,
    PointPlan,
    PointTask,
    AddInspectionPoint,
    EditInspectionPoint,
    DeleteInspectionPoint,
    InspectionPointQrCode
  },
  data() {
    return {
      inspectionPointInfo: {
        points: [],
        point: {},
        total: 0,
        records: 1,
        moreCondition: false,
        inspectionName: '',
        pointObjTypes: [],
        _currentTab: 'pointTaskDetail',
        conditions: {
          inspectionId: '',
          machineId: '',
          inspectionName: '',
          machineCode: '',
          pointObjType: '',
          communityId: '',
          page: 1,
          row: 1000
        }
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.inspectionPointInfo.conditions.communityId = this.communityId
    this.getDictData()
    this._listInspectionPoints()
  },
  methods: {
    async getDictData() {
      try {
        const data = await getDict('inspection_point', 'point_obj_type')
        this.inspectionPointInfo.pointObjTypes = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _listInspectionPoints() {
      try {
        const response = await listInspectionPoints(this.inspectionPointInfo.conditions)
        this.inspectionPointInfo.points = response.inspectionPoints
        this.inspectionPointInfo.total = response.total
        this.inspectionPointInfo.records = response.records

        if (response.inspectionPoints && response.inspectionPoints.length > 0) {
          this._switchInspectionPoint(response.inspectionPoints[0])
        }
      } catch (error) {
        console.error('获取巡检点列表失败:', error)
      }
    },
    _queryInspectionPointMethod() {
      this.inspectionPointInfo.conditions.page = 1
      this._listInspectionPoints()
    },
    _switchInspectionPoint(point) {
      this.inspectionPointInfo.point = point
      this.changeTab(this.inspectionPointInfo._currentTab)
    },
    changeTab(tab) {
      this.inspectionPointInfo._currentTab = tab

      // 触发子组件加载数据
      setTimeout(() => {
        if (this.$refs[tab]) {
          this.$refs[tab].loadData(this.inspectionPointInfo.point)
        }
      }, 500)

    },
    _openAddInspectionPointModal() {
      this.$refs.addInspectionPoint.open()
    },
    _openEditInspectionPointModel(point) {
      this.$refs.editInspectionPoint.open(point)
    },
    _openDeleteInspectionPointModel(point) {
      this.$refs.deleteInspectionPoint.open(point)
    },
    _pointQrCode(point) {
      console.log(point)
      this.$refs.inspectionPointQrCode.open(point)
    }
  }
}
</script>

<style scoped>
.inspection-point-container {
  padding: 20px;
}

.margin-bottom-20 {
  margin-bottom: 20px;
}

.margin-top {
  margin-top: 15px;
}

.margin-left-xs {
  margin-left: 10px;
}

.vc-org {
  padding: 5px;
  max-height: 600px;
  overflow-y: auto;
}

.point-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.point-list li {
  padding: 10px;
  cursor: pointer;
}

.point-list li:last-child {
  border-bottom: none;
}

.point-list li:hover {
  background-color: #f5f7fa;
}

.point-list li.active {
  background-color: #409eff;
  color: #ecf5ff;
  font-weight: bold;
}

.ibox-tools {
  float: right;
}

.form-group {
  margin-bottom: 15px;
}

.col-form-label {
  display: inline-block;
  margin-right: 10px;
}
</style>