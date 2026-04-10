<template>
  <div class="work-detail-file">
    <el-row :gutter="20">
      <el-col :span="4">
        <div class="tree-container">
          <el-scrollbar>
            <ul class="content-list">
              <li
                v-for="(item,index) in workDetailFileInfo.contents"
                :key="index"
                @click="swatchFileContentId(item)"
                :class="{'active-content': workDetailFileInfo.contentId === item.contentId}"
              >
                问题{{ item.seqNum }}
              </li>
            </ul>
          </el-scrollbar>
        </div>
      </el-col>
      <el-col :span="20">
        <el-row :gutter="20" class="margin-top-lg">
          <el-col :span="6">
            <el-input
              v-model.trim="workDetailFileInfo.staffNameLike"
              :placeholder="$t('workDetailFile.staffNamePlaceholder')"
              clearable
            />
          </el-col>
          <el-col :span="6">
            <el-date-picker
              v-model="workDetailFileInfo.queryStartTime"
              type="datetime"
              :placeholder="$t('workDetailFile.startTimePlaceholder')"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 100%"
            />
          </el-col>
          <el-col :span="6">
            <el-date-picker
              v-model="workDetailFileInfo.queryEndTime"
              type="datetime"
              :placeholder="$t('workDetailFile.endTimePlaceholder')"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 100%"
            />
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="queryWorkDetailFile">
              <i class="el-icon-search"></i>{{ $t('common.search') }}
            </el-button>
            <el-button @click="resetWorkDetailFile">
              <i class="el-icon-refresh"></i>{{ $t('common.reset') }}
            </el-button>
          </el-col>
        </el-row>

        <div class="margin-top">
          <el-table
            :data="workDetailFileInfo.files"
            border
            style="width: 100%"
            v-loading="loading"
          >
            <el-table-column
              prop="staffName"
              :label="$t('workDetailFile.staffName')"
              align="center"
            />
            <el-table-column
              :label="$t('workDetailFile.attachment')"
              align="center"
            >
              <template slot-scope="scope">
                <div v-if="scope.row.fileType !== 'S'">
                  <el-image
                    v-if="scope.row.pathUrl.endsWith('jpg') || scope.row.pathUrl.endsWith('png')"
                    style="width: 60px; height: 60px;"
                    :src="scope.row.pathUrl"
                    :preview-src-list="[scope.row.pathUrl]"
                    fit="cover"
                  />
                  <el-link v-else :href="scope.row.pathUrl" target="_blank">
                    {{ $t('common.download') }}
                  </el-link>
                </div>
                <span v-else>--</span>
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
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listWorkPoolFile } from '@/api/oa/workDetailApi'

export default {
  name: 'WorkDetailFile',
  data() {
    return {
      workDetailFileInfo: {
        files: [],
        contents: [],
        workId: '',
        contentId: '',
        staffNameLike: '',
        queryStartTime: '',
        queryEndTime: ''
      },
      loading: false,
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.workDetailFileInfo.workId = params.workId
      this.workDetailFileInfo.contents = params.contents
      if (params.contents.length > 0) {
        this.swatchFileContentId(params.contents[0])
      }
    },
    async loadWorkDetailFileData(page, size) {
      try {
        this.loading = true
        const params = {
          workId: this.workDetailFileInfo.workId,
          contentId: this.workDetailFileInfo.contentId,
          staffNameLike: this.workDetailFileInfo.staffNameLike,
          queryStartTime: this.workDetailFileInfo.queryStartTime,
          queryEndTime: this.workDetailFileInfo.queryEndTime,
          page,
          row: size
        }
        const { data, total } = await listWorkPoolFile(params)
        this.workDetailFileInfo.files = data
        this.page.total = total
      } catch (error) {
        console.error('获取流转附件列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    queryWorkDetailFile() {
      this.page.current = 1
      this.loadWorkDetailFileData(this.page.current, this.page.size)
    },
    swatchFileContentId(content) {
      this.workDetailFileInfo.contentId = content.contentId
      this.loadWorkDetailFileData(this.page.current, this.page.size)
    },
    resetWorkDetailFile() {
      this.workDetailFileInfo.staffNameLike = ''
      this.workDetailFileInfo.queryStartTime = ''
      this.workDetailFileInfo.queryEndTime = ''
      this.queryWorkDetailFile()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadWorkDetailFileData(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadWorkDetailFileData(this.page.current, this.page.size)
    }
  }
}
</script>

<style scoped>
.tree-container {
  height: 500px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 10px;
}

.content-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.content-list li {
  padding: 10px;
  margin-bottom: 5px;
  border-radius: 4px;
  cursor: pointer;
  text-align: center;
}

.content-list li:hover {
  background-color: #f5f7fa;
}

.active-content {
  background-color: #ecf5ff;
  color: #409eff;
  border: 1px solid #d9ecff;
}

.margin-top-lg {
  margin-top: 20px;
}
.margin-top {
  margin-top: 20px;
}
</style>