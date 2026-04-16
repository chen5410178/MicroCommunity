<template>
  <div class="margin-top">
    <el-row class="">
      <el-col :span="4" class="padding-right-xs padding-left-xl">
        <el-input
          v-model="ownerDetailCarInfo.carNum"
          :placeholder="$t('ownerDetailCar.pleaseEnterCarNumber')"
        />
      </el-col>
      <el-col :span="4" class="padding-right-xs padding-right-xl">
        <el-button
          type="primary"
          size="small"
          style="margin-left:10px"
          @click="_qureyOwnerDetailCar"
        >
          <i class="el-icon-search"></i>{{ $t('common.query') }}
        </el-button>
      </el-col>
      <el-col :span="16" class="text-right">
        <el-button
          type="primary"
          size="small"
          style="margin-left:10px"
          v-if="hasPrivilege('502023032813991677')"
          @click="_addOwnerCar"
        >
          {{ $t('ownerDetailCar.addCar') }}
        </el-button>
      </el-col>
    </el-row>
    <div class="margin-top">
      <el-table
        :data="ownerDetailCarInfo.cars"
        style="width: 100%"
        border
        stripe
      >
        <el-table-column
          prop="carNum"
          :label="$t('ownerDetailCar.carNumber')"
          align="center"
        />
        <el-table-column
          :label="$t('ownerDetailCar.licenseType')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.leaseType === 'T' ? $t('ownerDetailCar.temporaryCar') : scope.row.leaseTypeName }}
          </template>
        </el-table-column>
        <el-table-column
          prop="carTypeName"
          :label="$t('ownerDetailCar.carType')"
          align="center"
        />
        <el-table-column
          prop="carColor"
          :label="$t('ownerDetailCar.color')"
          align="center"
        />
        <el-table-column
          :label="$t('ownerDetailCar.owner')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.ownerName }}({{ scope.row.link }})
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('ownerDetailCar.parkingSpace')"
          align="center"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.areaNum && scope.row.state === '1001'">
              {{ scope.row.areaNum }}-{{ scope.row.num }}
            </span>
            <span v-else>
              {{ $t('ownerDetailCar.parkingSpaceReleased') }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('ownerDetailCar.validityPeriod')"
          align="center"
        >
          <template slot-scope="scope">
            <div v-if="scope.row.leaseType === 'H'">
              {{ scope.row.startTime }}<br>~{{ scope.row.endTime }}
            </div>
            <div v-else>-</div>
          </template>
        </el-table-column>
      </el-table>
      <el-row>
        <el-col :span="12" :offset="12">
          <el-pagination
            @current-change="handlePageChange"
            :current-page="pagination.currentPage"
            :page-size="pagination.pageSize"
            :total="pagination.total"
            layout="total, prev, pager, next, jumper"
            class="text-right"
          />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryOwnerCars } from '@/api/owner/ownerDetailCarApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerDetailCar',
  data() {
    return {
      ownerDetailCarInfo: {
        cars: [],
        ownerId: '',
        ownerName: '',
        carNum: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(ownerId, ownerName) {
      this.ownerDetailCarInfo.ownerId = ownerId
      this.ownerDetailCarInfo.ownerName = ownerName
      this._loadOwnerDetailCarData(1, 10)
    },
    _loadOwnerDetailCarData(page, row) {
      const params = {
        communityId: getCommunityId(),
        ownerId: this.ownerDetailCarInfo.ownerId,
        carNum: this.ownerDetailCarInfo.carNum,
        page,
        row
      }

      queryOwnerCars(params).then(res => {
        this.ownerDetailCarInfo.cars = res.data
        this.pagination = {
          currentPage: page,
          pageSize: row,
          total: res.total
        }
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    _qureyOwnerDetailCar() {
      this._loadOwnerDetailCarData(1, 10)
    },
    _addOwnerCar() {
      this.$router.push({
        path: '/views/car/hireParkingSpace',
        query: {
          ownerId: this.ownerDetailCarInfo.ownerId,
          ownerName: this.ownerDetailCarInfo.ownerName
        }
      })
    },
    handlePageChange(currentPage) {
      this._loadOwnerDetailCarData(currentPage, this.pagination.pageSize)
    },
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}
.margin-top-lg {
  margin-top: 30px;
}
.padding-right-xs {
  padding-right: 5px;
}
.padding-left-xl {
  padding-left: 20px;
}
.padding-right-xl {
  padding-right: 20px;
}
.text-right {
  text-align: right;
}
</style>