import { User } from "../../user/models/user";

export interface Task {
    id: number;
    tittle: string;
    description: string;
    completed: boolean;
    userId: number;
    createdAt: string;

};