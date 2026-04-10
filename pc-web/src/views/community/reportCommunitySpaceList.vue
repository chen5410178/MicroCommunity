<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="4">
        <el-card class="box-card">
          <div class="filter-container">
            <el-date-picker v-model="queryParams.appointmentTime" type="month"
              :placeholder="$t('reportCommunitySpace.selectMonth')" value-format="yyyy-MM" @change="fetchData" />
          </div>
          <el-scrollbar class="venue-scrollbar">
            <ul class="venue-items">
              <li v-for="(item, index) in venues" :key="index"
                :class="{ 'active': queryParams.venueId === item.venueId }" @click="selectVenue(item)">
                {{ item.name }}
              </li>
            </ul>
          </el-scrollbar>
        </el-card>
      </el-col>

      <el-col :span="20">
        <el-card class="box-card">
          <el-table :data="tableData" border style="width: 100%">
            <el-table-column prop="time" :label="$t('reportCommunitySpace.appointmentTime')" width="100"
              align="center" />
            <template v-if="communitySpaces.length > 0">
              <el-table-column v-for="space in communitySpaces" :key="space.spaceId" :label="space.name" align="center">
                <template slot-scope="{ $index }">
                  <div v-if="getSpaceStatus($index, space) === 'available'">
                    <el-button type="text" @click="openAddModal(space.spaceId, $index)">
                      {{ $t('reportCommunitySpace.available') }}
                    </el-button>
                  </div>
                  <div v-else>
                    {{ getSpaceStatus($index, space) }}
                  </div>
                </template>
              </el-table-column>
            </template>
            <el-table-column v-else :label="$t('reportCommunitySpace.noSpace')" align="center" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <add-community-space-person ref="addPersonDialog" @success="fetchData" />
  </div>
</template>

<script>
import { listCommunityVenue, listCommunitySpace, listCommunitySpacePerson } from '@/api/community/reportCommunitySpaceApi'
import AddCommunitySpacePerson from '@/components/community/addCommunitySpacePerson'
import { getCommunityId } from '@/api/community/communityApi'
import { getDateYYYYMMDD } from '@/utils/dateUtil'

export default {
  name: 'ReportCommunitySpaceList',
  components: { AddCommunitySpacePerson },
  data() {
    return {
      queryParams: {
        venueId: '',
        appointmentTime: getDateYYYYMMDD(),
        communityId: getCommunityId()
      },
      venues: [],
      communitySpaces: [],
      spacePersons: [],
      tableData: Array.from({ length: 24 }, (_, i) => ({ time: `${i}` }))
    }
  },
  created() {
    this.fetchVenues()
  },
  methods: {
    selectVenue(venue) {
      this.queryParams.venueId = venue.venueId
      this.fetchData()
    },
    async fetchVenues() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.queryParams.communityId
        }
        this.venues = await listCommunityVenue(params)
        if (this.venues.length > 0) {
          this.queryParams.venueId = this.venues[0].venueId
          this.fetchData()
        }
      } catch (error) {
        this.$message.error(this.$t('common.fetchFailed'))
      }
    },
    async fetchData() {
      if (!this.queryParams.venueId) return

      try {
        // 获取场地列表
        const spaceParams = {
          page: 1,
          row: 100,
          venueId: this.queryParams.venueId,
          communityId: this.queryParams.communityId
        }
        this.communitySpaces = await listCommunitySpace(spaceParams)

        // 获取预约记录
        const personParams = {
          page: 1,
          row: 100,
          venueId: this.queryParams.venueId,
          appointmentTime: this.queryParams.appointmentTime,
          communityId: this.queryParams.communityId,
          state: 'S'
        }
        this.spacePersons = await listCommunitySpacePerson(personParams)
      } catch (error) {
        this.$message.error(this.$t('common.fetchFailed'))
      }
    },
    getSpaceStatus(hour, space) {
      // 检查场地是否在该小时不可用
      const openTime = space.openTimes.find(t => t.hours == hour)
      if (openTime && openTime.isOpen === 'N') {
        return this.$t('reportCommunitySpace.notAvailable')
      }

      // 检查是否有预约
      const person = this.spacePersons.find(p => p.spaceId === space.spaceId)
      if (person) {
        const timeSlot = person.times.find(t => t.hours == hour)
        if (timeSlot) {
          return `${person.personName} > ${person.personTel}`
        }
      }

      return 'available'
    },
    openAddModal(spaceId, hour) {
      const data = {
        spaceId: spaceId,
        hours: hour,
        appointmentTime: this.queryParams.appointmentTime,
        openTime: hour
      }
      this.$refs.addPersonDialog.open(data)
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.box-card {
  margin-bottom: 20px;
}

.filter-container {
  margin-bottom: 20px;
}

.venue-list {
  max-height: calc(100vh - 200px);
  overflow-y: auto;
}

.venue-items {
  list-style: none;
  padding: 0;
  margin: 0;
}

.venue-items li {
  padding: 12px 15px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  transition: all 0.3s;
}

.venue-items li:hover {
  background-color: #f5f7fa;
}

.venue-items li.active {
  background-color: #409eff;
  color:#ecf5ff ;
  font-weight: bold;
}

.venue-radio {
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 4px;
  background: #f5f7fa;

  &:hover {
    background: #e4e7ed;
  }

  ::v-deep .el-radio__label {
    padding-left: 10px;
  }
}
</style>