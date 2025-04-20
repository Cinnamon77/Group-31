import { requestClient } from '#/api/request';

export namespace NoteApi {
  /** 笔记列表结果 */
  export interface NoteResult {
    id: number;
    userId: number;
    content: string;
    createdAt: string;
    updatedAt: string;
  }

  /** 新增笔记参数 */
  export interface NoteParams {
    content: string;
    class?: string;
  }
}

/**
 * 获取笔记列表
 */
export async function getNoteListApi() {
  return requestClient.get<NoteApi.NoteResult[]>('/note/list');
}

/**
 * 新增笔记
 */
export async function addNoteApi(data: NoteApi.NoteParams) {
  return requestClient.post<null>('/note', data);
}

/**
 * 删除笔记
 */
export async function deleteNoteApi(id: number) {
  return requestClient.delete<null>(`/note/${id}`);
}
