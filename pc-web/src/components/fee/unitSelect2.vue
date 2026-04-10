<template>
  <el-select
    v-model="selectedUnit"
    :placeholder="$t('meterWater.selectUnit')"
    filterable
    clearable
    style="width: 100%"
    :disabled="!floorId"
    @change="handleChange"
  >
    <el-option
      v-for="item in units"
      :key="item.unitId"
      :label="`${item.unitNum}单元`"
      :value="item.unitId"
    />
  </el-select>
</template>

<script>
import { queryUnits } from '@/api/fee/meterWaterManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'UnitSelect2',
  props: {
    value: {
      type: [String, Number],
      default: ''
    },
    floorId: {
      type: [String, Number],
      default: ''
    }
  },
  data() {
    return {
      units: [],
      selectedUnit: this.value,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  watch: {
    floorId: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.loadUnits(newVal)
        } else {
          this.units = []
          this.selectedUnit = ''
        }
      }
    },
    value(newVal) {
      this.selectedUnit = newVal
    }
  },
  methods: {
    async loadUnits(floorId) {
      try {
        const { data } = await queryUnits({
          floorId,
          communityId: this.communityId,
          page: 1,
          row: 50
        })
        this.units = data
      } catch (error) {
        console.error('Failed to load units:', error)
      }
    },
    handleChange(value) {
      const unit = this.units.find(item => item.unitId === value)
      this.$emit('change', {
        unitId: value,
        unitNum: unit ? unit.unitNum : ''
      })
    }
  }
}
</script>