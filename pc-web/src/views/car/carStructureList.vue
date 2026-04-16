<template>
  <div class="car-structure-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <floor-unit-tree ref="floorUnitTree" @switchFloorUnit="switchFloorUnit" />
      </el-col>
      <el-col :span="20">
        <el-card class="box-card margin-bottom car-list-card">
          <el-row :gutter="10" class="">
            <el-col v-for="(car, index) in carStructureInfo.cars" :key="index" :span="4"
              class="text-center margin-bottom  car-card margin-sm" :style="{ 'background-color': getBgColor(car) }"
              @dblclick.native="toSimplifyAcceptance(car)">
              <div>{{ car.areaNum }}-{{ car.num }}</div>
              <div>{{ car.carNum }}</div>
              <div>{{ car.floorNum }}-{{ car.unitNum }}-{{ car.roomNum }}</div>
              <div>{{ car.ownerName || $t('carStructure.noOwner') }}</div>
              <div>
                <span>{{ $t('carStructure.oweAmount') }}</span>：{{ car.oweAmount }}
                <span>{{ $t('carStructure.yuan') }}</span>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import floorUnitTree from '@/components/room/floorUnitTree'
import { listCarStructure } from '@/api/car/carStructureApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CarStructureList',
  components: {
    floorUnitTree
  },
  data() {
    return {
      carStructureInfo: {
        cars: []
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    setTimeout(() => {
      this.$refs.floorUnitTree.selectFirstUnit()
    }, 1000)
  },
  methods: {
    switchFloorUnit(data) {
      this.carStructureInfo.cars = []
      if (!data.unitId) {
        return
      }
      this.loadCars(data.unitId)
    },
    async loadCars(unitId) {
      try {
        const params = {
          page: 1,
          row: 100,
          unitId: unitId,
          communityId: this.communityId
        }
        const { data } = await listCarStructure(params)
        this.carStructureInfo.cars = data
      } catch (error) {
        this.$message.error(this.$t('carStructure.fetchError'))
      }
    },
    getBgColor(car) {
      if (!car.ownerName) {
        return "#1AB394"
      }
      if (car.oweAmount > 0) {
        return "#DC3545"
      }
      return "#1296db"
    },
    toSimplifyAcceptance(car) {
      this.$router.push('/pages/property/simplifyAcceptance?tab=业务受理&searchType=1&searchValue=' + `${car.floorNum}-${car.unitNum}-${car.roomNum}`)

    }
  }
}
</script>

<style lang="scss" scoped>
.car-structure-container {
  padding: 20px;

  .car-list-card {
    height: 100%;
    min-height: calc(100vh - 120px);
  }

  .car-card {
    color: #fff;
    border-radius: 5px;
    cursor: pointer;
    min-height: 120px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    transition: all 0.3s;

    &:hover {
      transform: scale(1.05);
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    }
  }

  .margin-top {
    margin-top: 10px;
  }

  .margin-bottom {
    margin-bottom: 20px;
  }

  .padding {
    padding: 10px;
  }

  .text-center {
    text-align: center;
  }
}
</style>